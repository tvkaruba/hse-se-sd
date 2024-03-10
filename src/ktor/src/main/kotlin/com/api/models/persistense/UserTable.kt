package com.api.models.persistense

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object UserTable: UUIDTable("users") {
    val name = varchar("name", length = 255)
    val email = varchar("email", length = 255)

    val created = datetime("createdAt").clientDefault { LocalDateTime.now() }
    val updated = datetime("updatedAt").nullable()
}