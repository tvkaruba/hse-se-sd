package com.api.controllers.error

import com.api.models.errors.AuthenticationException
import com.api.models.errors.UUIDFormatException
import com.api.models.errors.UserNotFoundException
import com.api.models.responses.ApiResponse
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun StatusPagesConfig.userErrorHandler() {
    exception(UserNotFoundException::handle)
    exception(UUIDFormatException::handle)

    exception<Throwable> { call, cause ->
        if (cause is AuthenticationException)
            call.respondText(text = "401: ${cause.message}", status = HttpStatusCode.Unauthorized)
        else
            call.respondText(text = "500: ${cause.message}", status = HttpStatusCode.InternalServerError)
    }
}