package com.api.controllers.error

import com.api.models.errors.UUIDFormatException
import com.api.models.errors.UserNotFoundException
import com.api.models.responses.ApiResponse
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun StatusPagesConfig.userErrorHandler() {
    exception(UserNotFoundException::handle)
    exception(UUIDFormatException::handle)

    exception<Throwable> { call, _ ->
        val status = HttpStatusCode.InternalServerError
        call.response.status(status)
        call.respond(ApiResponse.Error(status.description, status.value))
    }
}