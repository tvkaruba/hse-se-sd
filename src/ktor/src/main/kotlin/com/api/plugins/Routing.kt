package com.api.plugins

import com.api.controllers.basicRouting
import com.api.controllers.configureUserRoutes
import com.api.controllers.jwtRouting
import io.ktor.server.application.*

fun Application.configureRouting() {
    configureUserRoutes()
    basicRouting()
    jwtRouting()
}
