import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*

@Serializable
data class User(val id: Int, val name: String, val email: String)

object UsersTable : Table("users") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 128)
    val email = varchar("email", 128)

    override val primaryKey = PrimaryKey(id)
}