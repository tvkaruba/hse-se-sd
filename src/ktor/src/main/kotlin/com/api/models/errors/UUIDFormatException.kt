package com.api.models.errors

import com.api.models.responses.ApiResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class UUIDFormatException(val value: String?): Exception() {
    companion object {
        suspend fun handle(call: ApplicationCall, cause: UUIDFormatException) {
            val status = HttpStatusCode.BadRequest
            call.response.status(status)
            call.respond(ApiResponse.Error("value ${cause.value} could not be converted to UUID", status.value))
        }
    }
}