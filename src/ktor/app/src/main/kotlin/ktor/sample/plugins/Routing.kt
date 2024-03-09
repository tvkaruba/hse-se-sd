package ktor.sample.plugins

import io.ktor.network.sockets.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ktor.sample.routes.usersPagedRoute

fun Application.configureRouting() {
    routing {
        get("/json") {
            call.respond(mapOf("hello" to "world"))
        }

        usersPagedRoute()
    }
}