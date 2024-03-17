package com.api.plugins.auth

import com.api.models.errors.AuthenticationException
import io.ktor.server.auth.*
import io.ktor.util.*

object BasicAuthTypes {
    const val BASIC_AUTH = "auth-basic"
    const val BASIC_AUTH_HASHED = "auth-basic-hashed"
}

fun AuthenticationConfig.configureBasic() {

    val digestFunction = getDigestFunction("SHA-256") { "ktor${it.length}" }
    val hashedUserTable = UserHashedTableAuth(
        table = mapOf(
            "ktv" to digestFunction("12345"),
            "tkaruba" to digestFunction("qwerty")
        ),
        digester = digestFunction
    )

    basic(BasicAuthTypes.BASIC_AUTH) {
        realm = "ktor-api"
        validate { credentials ->
            if (credentials.name == credentials.password) {
                UserIdPrincipal(credentials.name)
            } else {
                throw AuthenticationException()
            }
        }
    }

    basic (BasicAuthTypes.BASIC_AUTH_HASHED){
        realm = "ktor-api"
        validate { credentials ->
            UserIdPrincipal(hashedUserTable.authenticate(credentials)?.name ?: throw AuthenticationException())
        }
    }
}