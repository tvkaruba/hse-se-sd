package com.api.controllers

import com.api.plugins.auth.BasicAuthTypes
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.basicRouting() {
    routing {
        route("/basic") {

            authenticate(BasicAuthTypes.BASIC_AUTH) {
                get {
                    call.respondText("Hello, ${call.principal<UserIdPrincipal>()?.name}!")
                }
            }

            authenticate(BasicAuthTypes.BASIC_AUTH_HASHED) {
                get("/hashed") {
                    call.respondText("Hello, ${call.principal<UserIdPrincipal>()?.name}!")
                }
            }
        }
    }
}