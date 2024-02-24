# Многопоточность

## Введение

Независимо от того, какой язык вы используете для написания своих программ, по умолчанию все они являются последовательными. То есть все инструкции, которые мы пишем, последовательно выполняются операционной системой. Следующая строка кода не может начать свое выполнение до завершения текущей и ждет, пока текущая строка не завершит свое выполнение.

Многозадачность (multitasking) — свойство операционной системы или среды выполнения обеспечивать возможность параллельной (или псевдопараллельной) обработки нескольких задач.

Многопоточность (multithreading) — свойство платформы (например, операционной системы, виртуальной машины и т. д.) или приложения, состоящее в том, что процесс, порождённый в операционной системе, может состоять из нескольких потоков, выполняющихся «параллельно», то есть без предписанного порядка во времени. При выполнении некоторых задач такое разделение может достичь более эффективного использования ресурсов вычислительной машины.

## Критические секции

Критическая секция — участок исполняемого кода программы, в котором производится доступ к общему ресурсу, который не должен быть одновременно использован более чем одним потоком выполнения.

Данный код запускает 100000 параллельно выполняющихся корутин. В данном примере синхронизация блоком synchronized будет работать корректно, и тест будет пройден. Модифицируем этот код, добавив suspend функцию delay внутрь блока synchronized... Что тогда произойдет?

```kt
fun main() = runBlocking {
    var obj = Any()
    var value = 0

    Array(100_000) {
        async(Dispatchers.Default) {
            synchronized(obj) {
                // delay(1)
                ++value
            }
        }
    }.forEach { it.await() }

    assertEquals(100_000, value)
}
```

**The 'delay' suspension point is inside a critical section**

Данный код даже не скомпилируется. Мы не можем вызывать никакие suspend функции внутри блока синхронизации, потому что текущий поток корутины должен уметь освобождаться для использования в других корутинах в момент начала активного ожидания. Стандартные реализации критической секции не рассчитаны на то, что в середине блока синхронизации поток может освободиться для других задач, а после ожидания suspend функции выполнение может быть продолжено на совершенно другом потоке.

То же самое касается и популярного в джаве класса ReentrantLock. С ним код скомпилируется, но синхронизация не будет работать, а может, даже случится IllegalMonitorStateException, но разработчика котлина сделали свои костыли вместо джавовских. хД Они разработали интерфейс Mutex, модифицируем код с его помощью:

```kt
fun main() = runBlocking {
    var value = 0
    val mutex = Mutex()

    Array(100_000) {
        async(Dispatchers.Default) {
            mutex.withLock {
                delay(1)
                ++value
            }
        }
    }.forEach { it.await() }

    assertEquals(100_000, value)
}
```

Такой код отработает корректно: операции над переменной value синхронизированы, suspend функция delay не мешает синхронизации. Вы можете сказать, что для данного примера синхронизация единственной переменной value при помощи критической секции — не самое концептуально правильное решение. И будете правы. Кто знает что бы тут стоило использовать?

## Атомарные переменные

Спойлер: volatile (@Volatile) и классы все классы из пакета атомик будут работать с корутинами не хуже, чем со стандартными потоками. А для чего нам нужен volatile?

Разные потоки для ускорения работы могут кэшировать у себя значения глобальных переменных. Из-за чего в других потоках при обращении к этим переменным мы не всегда будем видеть их актуальное значение.

```kt
@Volatile var value1 = true
@Volatile var value2 = true

fun main() = runBlocking {
    repeat(1_000) {
        value1 = true
        value2 = true

        val job1 = async(Dispatchers.Default) {
            value2 = false
            while(value1);
        }

        val job2 = async(Dispatchers.Default) {
            value1 = false
            while(value2);
        }

        job1.await()
        job2.await()
    }

    println("Success!")
}
```

Это правильный код, который успешно выполнится, но, если убрать аннотацию @Volatile хотя бы с одной переменной, то тест бесконечно зависнет в одном из циклов while, потому что поток корутины использует кэшированное значение общей переменной. Но рассмотрим другой пример:

```kt
@Volatile var value = true

fun main() = runBlocking {
    Array(10_000) {
        async(Dispatchers.Default) {
            value = !value
        }
    }.forEach { it.await() }

    assertTrue(value)
}
```

В данном тесте запускается 10 000 конкурирующих корутин, которые меняют значение value на противоположное. Так как количество корутин чётное, итоговое значение не должно измениться и должно остаться равным true. Однако в половине случаев тест падает, даже несмотря на @Volatile. А что случилось?

Операция value=!value — неатомарная, то есть состоит из нескольких операций: чтение value, потом запись нового value. Если неатомарные операции часто вызываются в любой многопоточной среде, есть риск получить неправильное значение переменной.

Большую гибкость по сравнению с @Volatile дают классы пакета java.util.concurrent.atomic, которые позволяют делать атомарными операции инкремента, декремента, compareAndSet (атомарно сравнить с предыдущим значением и записать), getAndSet (атомарное получение старого значения и запись нового) и ряд других.

```kt
fun main() = runBlocking {
    var value = AtomicInteger(0)

    Array(100_000) {
        async(Dispatchers.Default) {
            value.incrementAndGet()
        }
    }.forEach { it.await() }

    assertEquals(100_000, value.get())
}
```

Благодаря атомарности операции incrementAndGet в классе AtomicInteger синхронизация начинает работать правильно. Если задача не ограничивается настолько простыми операциями, и нужно синхронизировать целые участки кода, то что мы будем использовать?

Если использовать volatile и классы пакета java.util.concurrent.atomic правильно, то они будет корректно работать и в корутинах, но разработчики котлина сделали свои атомики... kotlinx.atomicfu - атомики для тех кому недостаточно jvm и андройд, и кому надо разрабатывать под мультиплатформу. Но вроде как интерфейс у них полностью идентичный.

## Реактивные переменные

Атомарные переменные — это хорошо, но часто необходимо также подписываться на изменение переменной и выполнять в обработчике какой-то код. В этом разделе мы рассмотрим реализации реактивных переменных, то есть классов, которые позволяют:

* через generic задать тип значения;
* записывать значение синхронно;
* получать значение синхронно;
* хранить это значение как внутреннее состояние;
* подписываться на изменения значения.

LiveData - позволяет хранить значение, получать и менять его напрямую через геттер\сеттер, подписываться на изменение значения, привязывать подписку к LifecycleOwner. Штука специфичная для андройда.

BehaviorSubject - похожая на LiveData штука из RxJava. Этот класс хранит значение, позволяет его менять, получать и подписываться на него. В то же время он дает возможность пользоваться всей функциональной мощью RxJava и может применяться для JVM, а не только для платформы Android. Еще один неоспоримый плюс: методы getValue и onNext (по сути setValue) синхронизированы и могут свободно использоваться в любой многопоточной среде, а во время подписки можно выбирать поток обработки с помощью метода observeOn. В целом ок, но для корутин есть свои штуки и тащить туда RxJava не особо нужно.

StateFlow - интерфейс StateFlow и его реализация являются частью Coroutines Flow и решают все проблемы LiveData и BehaviorSubject. Класс хранит значение, позволяет его получать, менять, а также подписываться на его изменение, как и в уже рассмотренных выше классах.

Не думаю что вам сейчас понадобится реактивщина, потом расскажу подробнее если вдруг у нас будет андройд в последнем модуле.

## Барьерная синхронизация

В корутинах пока еще нет стандартных реализаций барьерной синхронизации, поэтому тут будут велосипеды. Обычно под барьерной синхронизацией понимается следующее: несколько разных потоков ждут какого-то события, а когда оно происходит, потоки одновременно выходят из ожидания и продолжают свою работу. В случае с корутинами будет примерно то же самое, но ждать события будут не потоки, а, собственно, корутины. Значит, и метод ожидания будет suspend функцией.

Очень простой интерфейс всего с двумя функциями: одна ждет с таймаутом, вторая без.

```kt
interface Barrier {
    suspend fun await()

    @Throws(TimeoutCancellationException::class)
    suspend fun await(timeout: Duration)
}
```

Класс CountDownBarrier действует следующим образом: в одних потоках мы уменьшаем счетчик на 1, а в других потоках ожидаем, пока счетчик не станет равным 0.

```kt
class CountDownBarrier(count: UInt) : Barrier {
    private val stateFlow = MutableStateFlow(count)

    val counterValue: UInt
        @Synchronized get() = stateFlow.value

    @Synchronized
    fun countDown() {
        if (stateFlow.value > 0u) {
            --stateFlow.value
        }
    }

    override suspend fun await() {
        internalAwait()
    }

    @Throws(TimeoutCancellationException::class)
    override suspend fun await(timeout: Duration) {
        if (counterValue > 0u) {
            withTimeout(timeout) { internalAwait() }
        }
    }

    private suspend fun internalAwait() {
        if (counterValue > 0u) {
            // Await first value lower than 0 (suspend function).
            stateFlow.first { it <= 0u }
        }
    }
}
```

Класс CyclicBarrier похож на предыдущий, но значение внутреннего счетчика меняется не публичным методом countDown, а в зависимости от количества ожидающих потоков. Когда количество ожидающих потоков становится равно заданному значению, все они одновременно выходят из ожидания. После освобождения всех потоков внутренний счетчик возвращается в начальное значение.

```kt
class CoroutinesBarrier(val initialCoroutinesCount: UShort) : Barrier {
    private var stateFlow = MutableStateFlow(initialCoroutinesCount)

    val countLeftToReleaseBarrier: UShort
        @Synchronized get() = stateFlow.value

    override suspend fun await() {
        internalAwait()
    }

    @Throws(TimeoutCancellationException::class)
    override suspend fun await(timeout: Duration) {
        withTimeout(timeout) { internalAwait() }
    }

    private suspend fun internalAwait() {
        val (flowToAwait, countLeftToRelease) = countDownOrResetBarrier()

        if (countLeftToRelease > 0u) {
            // Await first value lower than 0 (suspend function).
            flowToAwait.first { it <= 0u }
        }
    }

    @Synchronized
    private fun countDownOrResetBarrier(): Pair<Flow<UShort>, UShort> {
        if (stateFlow.value > 0u) {
            --stateFlow.value
        }

        val result = stateFlow to stateFlow.value

        // Reset flow right before releasing awaiting coroutines.
        if (stateFlow.value <= 0u) {
            stateFlow = MutableStateFlow(initialCoroutinesCount)
        }

        return result
    }
}
```

## Параллельные коллекции

Ну, тут нам видимо предлагается использовать коллекции из джавы насколько я понимаю.

* Collections.synchronizedList(List<K>) является классом, который может использоваться для выполнения регулярных операций со списками (add, addAll, get, set, ...) методом синхронизации. Существуют также аналогичные реализации Map, Set и другие.
* CopyOnWriteArrayList может использоваться для обеспечения поточно-ориентированных операций модификации в List. Любая операция изменения сначала копирует весь список в локальную переменную с помощью итератора, выполняет действие, а затем заменяет его исходным списком. Эта коллекция примечательна тем, что обход списка не требует синхронизации.
* HashTable обеспечивает синхронизированный доступ к Map. Следует отметить, что синхронизируются только отдельные функции Map. Например, put не в их числе, поэтому безопасность потока на всей карте не гарантируется.
* ConcurrentHashMap<K,V> можно использовать для обеспечения безопасности потоков во всех методах HashMap. Важно отметить, что операции чтения (например, get) не блокируют Map целиком.

## Акторы

Помимо общих паттернов, корутины из Kotlin поощряют нас использовать стиль “обмен через коммуникацию”. В частности, для коммуникации хорошо подходит “актор”. Его можно использовать в корутинах, которые могут отправлять/принимать сообщения от него. Давайте посмотрим на примере:

```kt
sealed class CounterMsg {
    object IncCounter : CounterMsg() // one-way message to increment counter
    class GetCounter(val response: SendChannel<Int>) : CounterMsg() // a request with channel for reply.
}

fun counterActor() = actor<CounterMsg>(CommonPool) {
    var counter = 0 // actor state, not shared
    for (msg in channel) { // handle incoming messages
        when (msg) {
            is CounterMsg.IncCounter -> counter++
            is CounterMsg.GetCounter -> msg.response.send(counter)
        }
    }
}

suspend fun getCurrentCount(counter: ActorJob<CounterMsg>): Int {
    val response = Channel<Int>()
    counter.send(CounterMsg.GetCounter(response))
    val receive = response.receive()
    println("Counter = $receive")
    return receive
}

fun main(args: Array<String>) = runBlocking<Unit> {
    val counter = counterActor()

    launch(CommonPool) {
            while(getCurrentCount(counter) < 100){
                delay(100)
                println("sending IncCounter message")
                counter.send(CounterMsg.IncCounter)
            }
        }

    launch(CommonPool) {
        while ( getCurrentCount(counter) < 100) {
            delay(200)
        }
    }.join()
    counter.close() // shutdown the actor
}
```

На примере выше мы использовали Actor, который является корутиной сам по себе и может быть использован в любом контексте. Актор содержит текущее состояние приложения, которое содержится в counter. Тут мы также встречаем еще одну интересную функциональность Channel.

Channels предоставляют нам возможность обмена потоком значений, что очень похоже на то, как мы используем BlockingQueue (реализуя паттерн producer-consumer) в Java, только без всяких блокировок. Кроме того, send и receive являются прерываемыми функциями и используются для получения и отправки сообщений через канал, реализующий FIFO стратегию. Актор по умолчанию содержит в себе такой канал и может быть использован в других корутинах для передачи сообщений в него. В примере выше актор перебирает сообщения из своего собственного канала, обрабатывая их в соответствии с их типом.