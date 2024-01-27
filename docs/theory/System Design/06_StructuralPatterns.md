# Структурные паттерны

## Адаптер (Wrapper)

### Описание

Адаптер — это структурный паттерн проектирования, который позволяет объектам с несовместимыми интерфейсами работать вместе.

![StructuralPattern](/res/imgs/pattern10.png)

### Пример

![StructuralPattern](/res/imgs/pattern11.png)

```kt
interface Temperature {
    var temperature: Double
}

class CelsiusTemperature(override var temperature: Double) : Temperature

class FahrenheitTemperature(var celsiusTemperature: CelsiusTemperature) : Temperature {
    override var temperature: Double
        get() = convertCelsiusToFahrenheit(celsiusTemperature.temperature)
        set(temperatureInF) {
            celsiusTemperature.temperature = convertFahrenheitToCelsius(temperatureInF)
        }
    private fun convertFahrenheitToCelsius(f: Double): Double = (f - 32) * 5 / 9
    private fun convertCelsiusToFahrenheit(c: Double): Double = (c * 9 / 5) + 32
}

fun main(args: Array<String>) {
    val celsiusTemperature = CelsiusTemperature(0.0)
    val fahrenheitTemperature = FahrenheitTemperature(celsiusTemperature)
    celsiusTemperature.temperature = 36.6
    println("${celsiusTemperature.temperature} C -> ${fahrenheitTemperature.temperature} F")
    fahrenheitTemperature.temperature = 100.0
    println("${fahrenheitTemperature.temperature} F -> ${celsiusTemperature.temperature} C")
}
```

## Мост (Bridge)

### Описание

Мост — это структурный паттерн проектирования, который разделяет один или несколько классов на две отдельные иерархии — абстракцию и реализацию, позволяя изменять их независимо друг от друга.

![StructuralPattern](/res/imgs/pattern12.png)

### Пример

![StructuralPattern](/res/imgs/pattern13.png)

```kt
interface Switch {
    var appliance: Appliance
    fun turnOn()
}

interface Appliance {
    fun run()
}

class RemoteControl(override var appliance: Appliance) : Switch {
    override fun turnOn() = appliance.run()
}

class TV : Appliance {
    override fun run() = println("TV turned on")
}

class VacuumCleaner : Appliance {
    override fun run() = println("VacuumCleaner turned on")
}

fun main(args: Array<String>) {
    var tvRemoteControl = RemoteControl(appliance = TV())
    tvRemoteControl.turnOn()
    var fancyVacuumCleanerRemoteControl = RemoteControl(appliance = VacuumCleaner())
    fancyVacuumCleanerRemoteControl.turnOn()
}
```

## Компоновщик (Агрегат)

### Описание

Компоновщик — это структурный паттерн проектирования, который позволяет сгруппировать множество объектов в древовидную структуру, а затем работать с ней так, как будто это единичный объект.

![StructuralPattern](/res/imgs/pattern14.png)

### Пример

![StructuralPattern](/res/imgs/pattern15.png)

## Декоратор (Wrapper)

### Описание

Декоратор — это структурный паттерн проектирования, который позволяет динамически добавлять объектам новую функциональность, оборачивая их в полезные «обёртки».

На картинке ниже мы можем увидеть пример того, что получается без декораторов, выглядит страшно, да?

![StructuralPattern](/res/imgs/pattern16.png)

### Пример

![StructuralPattern](/res/imgs/pattern17.png)

Обратите внимание на ключевое слово `by`.

```kt
interface Comm {
    fun sendMessage(text: String): Result
}

class BtComm(val bt: BtModule) : Comm {
    override fun sendMessage(text: String): Result {
        return bt.send(text)
    }
}

class TcpComm(val tcpModule: TcpModule) : Comm {
    override fun sendMessage(text: String): Result {
        return tcpModule.send(text)
    }
}

class TcpModule {
    fun send(text: String): Result {
        println("sending message via TCP: $text")
        return Result.Success() // sealed class
    }
}
class BtModule {
    fun send(text: String): Result {
        println("sending message via BT: $text")
        return Result.Success()
    }
}

abstract class CommDecorator(comm: Comm) : Comm by comm

class JsonDecorator(comm: Comm) : CommDecorator(comm) {
    override fun sendMessage(text: String): Result {
		// with `super` method from abstract `CommDecorator` is called, 
		// that then delegates the call to the `comm` instance
        return super.sendMessage("{\"message\":\"$text\"}")
    }
}

fun main() {
    val tcpModule = TcpModule()
    val btModule = BtModule()

    val message = "hello"

    val tcpJsonComm: Comm = JsonDecorator(TcpComm(tcpModule))
    tcpJsonComm.sendMessage(message) // sending message via TCP: {"message":"hello"}

    val btJsonComm: Comm = JsonDecorator(BtComm(btModule))
    btJsonComm.sendMessage(message) // sending message via BT: {"message":"hello"}
}
```

## Фасад

### Описание

Фасад — это структурный паттерн проектирования, который предоставляет простой интерфейс к сложной системе классов, библиотеке или фреймворку.

### Пример

```kt
class CPU {
    fun freeze() = println("Freezing.")

    fun jump(position: Long) = println("Jump to $position.")

    fun execute() = println("Executing.")
}

class HardDrive {
    fun read(lba: Long, size: Int): ByteArray = byteArrayOf()
}

class Memory {
    fun load(position: Long, data: ByteArray) = println("Loading from memory position: $position")
}

// Facade
class Computer(val processor: CPU = CPU(), val ram: Memory = Memory(), val hd: HardDrive = HardDrive()) {
    companion object {
        val BOOT_ADDRESS = 0L
        val BOOT_SECTOR = 0L
        val SECTOR_SIZE = 0
    }

    fun start() {
        processor.freeze()
        ram.load(BOOT_ADDRESS, hd.read(BOOT_SECTOR, SECTOR_SIZE))
        processor.jump(BOOT_ADDRESS)
        processor.execute()
    }
}

fun main(args: Array<String>) {
    val computer = Computer()
    computer.start()
}
```

## Легковес (Кэш)

### Описание

Легковес — это структурный паттерн проектирования, который позволяет вместить большее количество объектов в отведённую оперативную память. Легковес экономит память, разделяя общее состояние объектов между собой, вместо хранения одинаковых данных в каждом объекте.

### Пример

![StructuralPattern](/res/imgs/pattern18.png)

```kt
interface Shape {
    fun draw(x: Int, y: Int, color: String)
}

class Circle(private val radius: Int) : Shape {
    override fun draw(x: Int, y: Int, color: String) {
        println("Drawing Circle at ($x,$y) with radius $radius and color $color")
    }
}

object ShapeFactory {
    private val circleMap = mutableMapOf<Int, Circle>()
    fun getCircle(radius: Int): Circle {
        if (!circleMap.containsKey(radius)) {
            circleMap[radius] = Circle(radius)
        }
        return circleMap[radius]!!
    }
}
```

## Заместитель (Proxy)

### Описание

Заместитель — это структурный паттерн проектирования, который позволяет подставлять вместо реальных объектов специальные объекты-заменители. Эти объекты перехватывают вызовы к оригинальному объекту, позволяя сделать что-то до или после передачи вызова оригиналу.

### Пример

![StructuralPattern](/res/imgs/pattern19.png)

```kt
interface Database {
    fun query(dbQuery: String): String
}

class RealDatabase : Database {
    override fun query(dbQuery: String): String {
        return "Executing query: $dbQuery"
    }
}

class ProxyDatabase : Database {
    private val realDatabase = RealDatabase()
    private val restrictedQueries = listOf("DROP", "DELETE")
    override fun query(dbQuery: String): String {
        if (restrictedQueries.any { dbQuery.contains(it, ignoreCase = true) }) {
            return "Query not allowed!"
        }
        
        println("Logging: $dbQuery")
        return realDatabase.query(dbQuery)
    }
}
```