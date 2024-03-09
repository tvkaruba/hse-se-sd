package ktor.sample.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ktor.sample.services.UserService
import org.kodein.di.ktor.di

fun Route.usersPagedRoute() {
    val userService by di().instance<UserService>()

    get("/user") {
        val users = userService.getAllBooks()
        call.respond(users)
    }
}