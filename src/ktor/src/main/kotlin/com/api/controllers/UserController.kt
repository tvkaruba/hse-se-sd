package com.api.controllers

import com.api.models.errors.UUIDFormatException
import com.api.models.requests.CreateUserRequest
import com.api.models.responses.ApiResponse
import com.api.models.responses.UserResponse
import com.api.services.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Application.configureUserRoutes() {
    val userService by inject<UserService>()

    routing {
        route("/user") {
            post {
                val request = call.receive<CreateUserRequest>()
                val newUser = userService.createNewUser(request.name, request.email)
                call.respond(
                    HttpStatusCode.Created,
                    ApiResponse.Data(UserResponse(id = newUser.id.toString(), name = newUser.name, email = newUser.email)))
            }

            get {
                val users = userService.getAllUsers()
                call.respond(
                    HttpStatusCode.OK,
                    ApiResponse.Data(users.map { UserResponse(it.id.toString(), it.name, it.email) }))
            }

            get("/{id}") {
                val id = call.parameters["id"].uuid()
                val user = userService.getUserById(id)
                call.respond(
                    HttpStatusCode.OK,
                    ApiResponse.Data(UserResponse(id = user.id.toString(), name = user.name, email = user.email)))
            }

            delete("/{id}") {
                val id = call.parameters["id"].uuid()
                userService.deleteUserById(id)
                call.response.status(HttpStatusCode.NoContent)
            }
        }
    }
}

private fun String?.uuid(): UUID {
    try {
        return UUID.fromString(this)
    } catch (e: IllegalArgumentException) {
        throw UUIDFormatException(this)
    }
}
