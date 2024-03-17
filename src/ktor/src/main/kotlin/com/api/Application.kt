package com.api

import com.api.plugins.*
import com.api.plugins.auth.configureAuthentication
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureOpenAPI()
    configureSerialization()
    configureDatabase()
    configureAuthentication()
    configureRouting()
    configureErrorHandling()
    configureKoin()
}
