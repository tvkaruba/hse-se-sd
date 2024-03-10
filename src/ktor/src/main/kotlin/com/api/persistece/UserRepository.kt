package com.api.persistece

import com.api.models.domain.User
import com.api.models.errors.UserNotFoundException
import com.api.models.persistense.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.*

class UserRepository : Repository.Create<User>, Repository.Read<User>, Repository.Delete<User> {
    private fun ResultRow.toDomain() = User(
        id = this[UserTable.id].value,
        name = this[UserTable.name],
        email = this[UserTable.email],
    )

    override fun getAll(): List<User> = transaction {
        UserTable
            .selectAll()
            .mapNotNull { it.toDomain() }
            .toList()
    }

    override fun getById(id: UUID): User = transaction {
        UserTable
            .selectAll().where { UserTable.id eq id }
            .firstOrNull()
            ?.toDomain() ?: throw UserNotFoundException(id)
    }

    override fun create(entity: User): User = transaction {
        UserTable
            .insert {
                it[email] = entity.email
                it[name] = entity.name
            }.resultedValues!!
            .first()
            .toDomain()
    }

    override fun deleteById(id: UUID) = transaction {
        UserTable.deleteWhere { UserTable.id eq id }
    }
}