package com.example.core.router

import com.example.features.user.models.User
import com.example.features.user.services.UserService
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*

fun Route.userRout(userService: UserService) {
    route("/user") {
        get("/") {
            userService.getAllUsers()?.let {
                call.respond(it)
            }
        }
        get("/{id}") {
            val candidate = call.parameters["id"]?.toIntOrNull()

            when (candidate) {
                null -> call.respond(HttpStatusCode.BadRequest, Error("ID must be long"))
                else -> {
                    val user = userService.getUser(candidate)
                    when (user) {
                        null -> call.respond(HttpStatusCode.NotFound, Error("User id $candidate not found"))
                        else -> call.respond(user)
                    }
                }
            }
        }
        post("/") {
            val userReceive = call.receive<User>()
            userService.postUser(userReceive)?.let { user ->
                call.respond(HttpStatusCode.Created, user)
            } ?: run {
                call.respond(HttpStatusCode.BadRequest, Error("User don't create"))
            }
        }
        put("/") {
            val userReceive = call.receive<User>()
            val updated = userService.updateUser(userReceive)
            if (updated == null) call.respond(HttpStatusCode.NotFound)
            else call.respond(HttpStatusCode.OK, updated)
        }
        delete("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalStateException("Must provide id");
            val removed = userService.deleteUser(id)
            if (removed) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.NotFound)
        }
    }
}