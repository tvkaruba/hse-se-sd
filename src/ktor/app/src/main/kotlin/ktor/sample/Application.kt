package ktor.sample

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ktor.sample.data.UserRepository
import ktor.sample.data.connectToPostgres
import ktor.sample.plugins.configureHTTP
import ktor.sample.plugins.configureSerialization
import ktor.sample.services.UserService

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureOpenAPI()
    configureMonitoring()
    configureSerialization()
    configureDatabases()
    configureRouting()
    configureErrorHandling()
    configureOpenTelemetry()
    configureKoin()
}