package com.api.plugins.auth

import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureAuthentication() {
    install(Authentication) {
        configureBasic()
        configureJWT()
    }
}
