package com.api.plugins

import io.ktor.server.application.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabase() {
    val config = environment.config

    val url = config.property("storage.url").getString()
    val driver = config.property("storage.driver").getString()
    val user = config.property("storage.user").getString()
    val password = config.property("storage.password").getString()

    Flyway.configure()
        .driver(driver)
        .dataSource(url, user, password)
        .load()
        .migrate()

    Database.connect(
        url = url,
        user = user,
        password = password,
        driver = driver
    )
}
