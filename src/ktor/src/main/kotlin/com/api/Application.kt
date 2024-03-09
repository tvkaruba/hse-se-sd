package com.api

import com.api.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureOpenAPI()
    configureSerialization()
    configureDatabase()
    configureRouting()
    configureErrorHandling()
    configureKoin()
}
