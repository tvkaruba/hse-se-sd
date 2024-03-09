package ktor.sample.data

import User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class UserRepository() : Repository.Create<User>, Repository.Read<User> {
    private fun toUser(row: ResultRow) = User(
        id = row[UsersTable.id],
        name = row[UsersTable.name],
        email = row[UsersTable.email],
    )

    override suspend fun create(entity: User): User? = dbQuery {
        val insertStatement = UsersTable.insert {
            it[UsersTable.name] = entity.name
            it[UsersTable.email] = entity.email
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::toUser)
    }

    override suspend fun getAll(page: Int, limit: Int): List<User> = dbQuery {
        val offset = (page - 1) * limit
        UsersTable
            .selectAll()
            .limit(limit, offset.toLong())
            .map(::toUser)
            .toList()
    }

    override suspend fun getById(id: Int): User? = dbQuery {
        UsersTable
            .selectAll()
            .where { UsersTable.id eq id }
            .map(::toUser)
            .singleOrNull()
    }
}