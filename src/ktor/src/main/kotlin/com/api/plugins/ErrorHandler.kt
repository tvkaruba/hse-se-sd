package com.api.plugins

import com.api.controllers.error.userErrorHandler
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*

fun Application.configureErrorHandling() {
    install(StatusPages) {
        userErrorHandler()
    }
}