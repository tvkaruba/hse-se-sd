# Сериализация/десериализация в Котлине

## Основы

### Сериализация в JSON

Для сериализации объекта в JSON мы используем встроенную функцию `Json.encodeToString()`, сериализуемый класс должен быть помечен атрибутом `@Serializable`. Также в примере показан простейший пример записи в файл.

```kt
@Serializable
class Project(val name: String, val language: String)

fun main() {
    val data = Project("kotlinx.serialization", "Kotlin")
    val jsonString = Json.encodeToString(data)
    println(jsonString)

    val file = File("file.txt")
    val writer = FileWriter(file)
    writer.write(jsonString)
    writer.close()
}
```

### Десереализация из JSON

Для десериализации объекта из JSON мы используем встроенную функцию `Json.decodeFromString<Type>()`. Также в примере показан простейший пример чтения из файла.

```kt
@Serializable
data class Project(val name: String, val language: String)

fun main() {
    val file = File("file.txt")
    val content = file.readText()

    val data = Json.decodeFromString<Project>(content)
    println(data)
}
```

## Ссылки

* https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/serialization-guide.md