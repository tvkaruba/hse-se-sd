package com.api.models.errors

import com.api.models.responses.ApiResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import java.util.UUID

class UserNotFoundException(private val id: UUID): Exception() {
    companion object {
        suspend fun handle(call: ApplicationCall, cause: UserNotFoundException) {
            val status = HttpStatusCode.NotFound
            call.response.status(status)
            call.respond(ApiResponse.Error("user with id: ${cause.id} does not exist!", status.value))
        }
    }
}