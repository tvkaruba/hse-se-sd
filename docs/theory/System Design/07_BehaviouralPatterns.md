# Поведенческие паттерны

## Цепочка обязанностей (Chain of Responsibility)

### Описание

Цепочка обязанностей — это поведенческий паттерн проектирования, который позволяет передавать запросы последовательно по цепочке обработчиков. Каждый последующий обработчик решает, может ли он обработать запрос сам и стоит ли передавать запрос дальше по цепи.

![StructuralPattern](/res/imgs/pattern20.png)

### Пример

![StructuralPattern](/res/imgs/pattern21.png)

```kt
interface Handler {
    var nextHandler: Handler?
    fun handleRequest(request: String): Boolean
}

class FirstHandler : Handler {
    override var nextHandler: Handler? = null
    override fun handleRequest(request: String): Boolean {
        if (request == "Request1") {
            println("FirstHandler handled $request")
            return true
        }
        return nextHandler?.handleRequest(request) ?: false
    }
}

class SecondHandler : Handler {
    override var nextHandler: Handler? = null
    override fun handleRequest(request: String): Boolean {
        if (request == "Request2") {
            println("SecondHandler handled $request")
            return true
        }
        return nextHandler?.handleRequest(request) ?: false
    }
}

fun main() {
    val firstHandler = FirstHandler()
    val secondHandler = SecondHandler()
    firstHandler.nextHandler = secondHandler
    listOf("Request1", "Request2", "Request3").forEach {
        if (!firstHandler.handleRequest(it)) {
            println("$it was not handled")
        }
    }
}
```

## Команда

### Описание

Команда — это поведенческий паттерн проектирования, который превращает запросы в объекты, позволяя передавать их как аргументы при вызове методов, ставить запросы в очередь, логировать их, а также поддерживать отмену операций.

### Пример

![StructuralPattern](/res/imgs/pattern22.png)

```kt
interface Command {
    fun execute()
}

class OnCommand(private val ce: ConsumerElectronics) : Command {
    override fun execute() {
        ce.on()
    }
}

class MuteAllCommand(internal var ceList: List<ConsumerElectronics>) : Command {
    override fun execute() {
        for (ce in ceList) {
            ce.mute()
        }
    }
}

interface ConsumerElectronics {
    fun on()
    fun mute()
}

class Television : ConsumerElectronics {
    override fun on() {
        println("Television is on!")
    }

    override fun mute() {
        println("Television is muted!")
    }
}

class SoundSystem : ConsumerElectronics {
    override fun on() {
        println("Sound system is on!")
    }

    override fun mute() {
        println("Sound system is muted!")
    }
}

class Button(var c: Command) {
    fun click() {
        c.execute()
    }
}

class UniversalRemote {
    fun getActiveDevice() : ConsumerElectronics{
        val tv = Television()
        return tv
    }
}

fun main(args: Array<String>) {
    val ce = UniversalRemote.getActiveDevice()
    val onCommand = OnCommand(ce)
    val onButton = Button(onCommand)
    onButton.click()

    val tv = Television()
    val ss = SoundSystem()
    val all = ArrayList<ConsumerElectronics>()
    all.add(tv)
    all.add(ss)
    val muteAll = MuteAllCommand(all)
    val muteAllButton = Button(muteAll)
    muteAllButton.click()
}
```

## Итератор

### Описание

Итератор — это поведенческий паттерн проектирования, который даёт возможность последовательно обходить элементы составных объектов, не раскрывая их внутреннего представления.

### Пример

![StructuralPattern](/res/imgs/pattern23.png)

```kt
class Novella(val name: String)

class Novellas(val novellas: MutableList<Novella> = mutableListOf()) : Iterable<Novella> {
    override fun iterator(): Iterator<Novella> = NovellasIterator(novellas)
}

class NovellasIterator(val novellas: MutableList<Novella> = mutableListOf(), var current: Int = 0) : Iterator<Novella> {
    override fun hasNext(): Boolean = novellas.size > current
    override fun next(): Novella {
        val novella = novellas[current]
        current++
        return novella
    }
}

fun main(args: Array<String>) {
    val novellas = Novellas(mutableListOf(Novella("Test1"), Novella("Test2")))
    novellas.forEach { println(it.name) }
}
```

## Посредник (Mediator)

### Описание

Посредник — это поведенческий паттерн проектирования, который позволяет уменьшить связанность множества классов между собой, благодаря перемещению этих связей в один класс-посредник.

![StructuralPattern](/res/imgs/pattern24.png)

### Пример

![StructuralPattern](/res/imgs/pattern25.png)

```kt
interface Command {
    fun land()
}

class Flight(private val atcMediator: IATCMediator) : Command {
    override fun land() {
        if (atcMediator.isLandingOk) {
            println("Landing done....")
            atcMediator.setLandingStatus(true)
        } else
            println("Will wait to land....")
    }

    fun getReady() {
        println("Getting ready...")
    }
}

class Runway(private val atcMediator: IATCMediator) : Command {
    init {
        atcMediator.setLandingStatus(true)
    }

    override fun land() {
        println("Landing permission granted...")
        atcMediator.setLandingStatus(true)
    }
}

interface IATCMediator {
    val isLandingOk: Boolean
    fun registerRunway(runway: Runway)
    fun registerFlight(flight: Flight)
    fun setLandingStatus(status: Boolean)
}

class ATCMediator : IATCMediator {
    private var flight: Flight? = null
    private var runway: Runway? = null

    override var isLandingOk: Boolean = false

    override fun registerRunway(runway: Runway) {
        this.runway = runway
    }

    override fun registerFlight(flight: Flight) {
        this.flight = flight
    }

    override fun setLandingStatus(status: Boolean) {
        isLandingOk = status
    }
}

fun main(args: Array<String>) {
    val atcMediator = ATCMediator()
    val sparrow101 = Flight(atcMediator)
    val mainRunway = Runway(atcMediator)
    atcMediator.registerFlight(sparrow101)
    atcMediator.registerRunway(mainRunway)
    sparrow101.getReady()
    mainRunway.land()
    sparrow101.land()
}
```

## Наблюдатель (Observer)

### Описание

Наблюдатель — это поведенческий паттерн проектирования, который создаёт механизм подписки, позволяющий одним объектам следить и реагировать на события, происходящие в других объектах.

![StructuralPattern](/res/imgs/pattern26.png)

### Пример

![StructuralPattern](/res/imgs/pattern27.png)

```kt
open class Subject {
    private var observers = mutableListOf<Observer>()

    fun callObservers() {
        for(obs in observers) obs.update()
    }

    fun attach(obs : Observer) {
        observers.add(obs)
    }

    fun detach(obs : Observer) {
        observers.remove(obs)
    }
}

class Sensor : Subject() {
    var temperature: Int = 0
        set(value) {
            field = value
            callObservers()
        }
}

interface Observer {
    fun update()
}

class Monitor(val sensor: Sensor) : Observer {
    init {
        sensor.attach(this)
    }
    
    override fun update() {
        val newTemperature = sensor.temperature
        println("update Monitor")
    }
}

fun main() {
    val sensor = Sensor()
    val monitor = Monitor(sensor)
    sensor.temperature = 5
}
```

А можно использовать встроенный делегат...

```kt
class Sensor  {
    var temperature: Int by Delegates.observable(0) { property, oldValue, newValue -> onChange()}
    
    private fun onChange() {

    }
}
```

## Снимок (Memento)

### Описание

Снимок — это поведенческий паттерн проектирования, который позволяет сохранять и восстанавливать прошлые состояния объектов, не раскрывая подробностей их реализации.

### Пример

![StructuralPattern](/res/imgs/pattern28.png)

```kt
class Memento(val state: String)

class Originator {
    var state: String? = null

    fun createMemento(): Memento {
        return Memento(state!!)
    }

    fun setMemento(memento: Memento) {
        state = memento.state
    }
}

class Caretaker {
    private val statesList = ArrayList<Memento>()

    fun addMemento(m: Memento) {
        statesList.add(m)
    }

    fun getMemento(index: Int): Memento {
        return statesList.get(index)
    }
}
```

## Состояние (State)

### Описание

Состояние — это поведенческий паттерн проектирования, который позволяет объектам менять поведение в зависимости от своего состояния. Извне создаётся впечатление, что изменился класс объекта.

![StructuralPattern](/res/imgs/pattern29.png)

### Пример

![StructuralPattern](/res/imgs/pattern30.png)

```kt
interface MobileAlertState {
    fun alert(ctx: AlertStateContext)
}

class AlertStateContext {
    private var currentState: MobileAlertState? = null

    init {
        currentState = Vibration()
    }

    fun setState(state: MobileAlertState) {
        currentState = state
    }

    fun alert() {
        currentState!!.alert(this)
    }
}

class Vibration : MobileAlertState {
    override fun alert(ctx: AlertStateContext) {
        println("vibration...")
    }
}

class Silent : MobileAlertState {
    override fun alert(ctx: AlertStateContext) {
        println("silent...")
    }
}

class Sound : MobileAlertState {
    override fun alert(ctx: AlertStateContext) {
        println("tu..tu..tu..tu")
    }
}

fun main(args: Array<String>) {
    val stateContext = AlertStateContext()
    stateContext.alert()
    stateContext.setState(Silent())
    stateContext.alert()
    stateContext.setState(Sound())
    stateContext.alert()
}
```

## Стратегия

### Описание

Стратегия — это поведенческий паттерн проектирования, который определяет семейство схожих алгоритмов и помещает каждый из них в собственный класс, после чего алгоритмы можно взаимозаменять прямо во время исполнения программы.

![StructuralPattern](/res/imgs/pattern31.png)

### Пример

![StructuralPattern](/res/imgs/pattern32.png)

```kt
class Printer(val strategy: (String) -> String) {
    fun print(string: String): String = strategy(string)
}

val lowerCaseFormatter: (String) -> String = String::toLowerCase

val upperCaseFormatter: (String) -> String = String::toUpperCase

fun main(args: Array<String>) {
    val lower = Printer(strategy = lowerCaseFormatter)
    println(lower.print("O tempora, o mores!"))
    val upper = Printer(strategy = upperCaseFormatter)
    println(upper.print("O tempora, o mores!"))
}
```

## Шаблонный метод

### Описание

Шаблонный метод — это поведенческий паттерн проектирования, который определяет скелет алгоритма, перекладывая ответственность за некоторые его шаги на подклассы. Паттерн позволяет подклассам переопределять шаги алгоритма, не меняя его общей структуры.

### Пример

![StructuralPattern](/res/imgs/pattern33.png)

```kt
abstract class DataMiner {
    fun mineData() {
        extractData()
        analyzeData()
        generateReport()
    }
    abstract fun extractData()
    abstract fun analyzeData()
    fun generateReport() {
        println("Generating report based on analysis.")
    }
}

class CSVDataMiner : DataMiner() {
    override fun extractData() {
        println("Extracting data from CSV file.")
    }
    override fun analyzeData() {
        println("Analyzing CSV data.")
    }
}

class XMLDataMiner : DataMiner() {
    override fun extractData() {
        println("Extracting data from XML file.")
    }
    override fun analyzeData() {
        println("Analyzing XML data.")
    }
}

fun main() {
    val csvMiner = CSVDataMiner()
    csvMiner.mineData()
    val xmlMiner = XMLDataMiner()
    xmlMiner.mineData()
}
```

## Посетитель (Visitor)

### Описание

Посетитель — это поведенческий паттерн проектирования, который позволяет добавлять в программу новые операции, не изменяя классы объектов, над которыми эти операции могут выполняться.

### Пример

![StructuralPattern](/res/imgs/pattern34.png)

```kt
interface PlanetVisitor {
    fun visit(planet: PlanetAlderaan)
    fun visit(planet: PlanetCoruscant)
    fun visit(planet: PlanetTatooine)
    fun visit(planet: MoonJedah)
}

interface Planet {
    fun accept(visitor: PlanetVisitor)
}

class MoonJedah : Planet {
    override fun accept(visitor: PlanetVisitor) = visitor.visit(planet = this)
}

class PlanetAlderaan : Planet {
    override fun accept(visitor: PlanetVisitor) = visitor.visit(planet = this)
}

class PlanetCoruscant : Planet {
    override fun accept(visitor: PlanetVisitor) = visitor.visit(planet = this)
}

class PlanetTatooine : Planet {
    override fun accept(visitor: PlanetVisitor) = visitor.visit(planet = this)
}

class NameVisitor(var name: String = "") : PlanetVisitor {
    override fun visit(planet: PlanetAlderaan) {
        name = "Alderaan"
    }

    override fun visit(planet: PlanetCoruscant) {
        name = "Coruscant"
    }


    override fun visit(planet: PlanetTatooine) {
        name = "Tatooine"
    }

    override fun visit(planet: MoonJedah) {
        name = "Jedah"
    }
}

fun main(args: Array<String>) {
    val planets = mutableListOf(PlanetAlderaan(), PlanetCoruscant(), PlanetTatooine(), MoonJedah())
    val visitor = NameVisitor()
    planets.forEach {
        it.accept(visitor)
        println(visitor.name)
    }
}
```