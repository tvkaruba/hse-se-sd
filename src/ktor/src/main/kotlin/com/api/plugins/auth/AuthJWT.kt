package com.api.plugins.auth

import com.api.models.errors.AuthenticationException
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.util.*

object JWTAuth {
    const val JWT_AUTH_NAME = "jwt-auth"
    const val SECRET = "secret"
    const val ISSUER = "http://0.0.0.0:8000/"
    const val AUDIENCE = "http://0.0.0.0:8000/hello"
    const val REALM = "Access to 'hello'"
}

fun AuthenticationConfig.configureJWT() {
    jwt(JWTAuth.JWT_AUTH_NAME) {
        realm = JWTAuth.REALM

        verifier (
            JWT.require(Algorithm.HMAC256(JWTAuth.SECRET))
                .withAudience(JWTAuth.AUDIENCE)
                .withIssuer(JWTAuth.ISSUER)
                .build()
        )

        validate { credential ->
            if (credential.payload.audience.contains(JWTAuth.AUDIENCE))
                JWTPrincipal(credential.payload)
            else throw AuthenticationException()
        }
    }
}

fun createToken(name: String): String {
    return JWT.create()
        .withAudience(JWTAuth.AUDIENCE)
        .withIssuer(JWTAuth.ISSUER)
        .withClaim("username", name)
        .withExpiresAt(Date(System.currentTimeMillis() + 60000))
        .sign(Algorithm.HMAC256(JWTAuth.SECRET))
}