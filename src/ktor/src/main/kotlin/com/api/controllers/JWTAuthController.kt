package com.api.controllers

import com.api.models.requests.CreateUserRequest
import com.api.plugins.auth.JWTAuth
import com.api.plugins.auth.createToken
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

//data class User(val username: String, val password: String)

fun Application.jwtRouting() {
    routing {
        route("/JWT") {

            post("/login") {
                val request = call.receive<CreateUserRequest>()
                if (request.name == request.email) {
                    val token = createToken(request.name)
                    call.respondText(token)
                } else {
                    call.respondText("Invalid user")
                }
            }

            authenticate(JWTAuth.JWT_AUTH_NAME) {
                get {
                    val principal = call.principal<JWTPrincipal>()
                    val username = principal!!.payload.getClaim("username").asString()
                    val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                    call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
                }
            }
        }
    }
}