package com.api.plugins

import com.api.controllers.configureUserRoutes
import io.ktor.server.application.*

fun Application.configureRouting() {
    configureUserRoutes()
}
