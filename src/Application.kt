package com.example

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.gson.*
import io.ktor.features.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        gson {
        }
    }

    user()
}

data class User(val id: Long, val name: String, val surname: String)

val users = mutableListOf<User>()

fun Application.user(){
   routing {
       route("/user"){
           get { call.respond(users)}
           get("/{id}") {
               val candidate = call.parameters["id"]?.toLongOrNull()

               when (candidate){
                   null -> call.respond(HttpStatusCode.BadRequest, Error("ID must be long"))
                   else -> {
                       val user = users.firstOrNull { it.id == candidate}
                       when(user){
                           null -> call.respond(HttpStatusCode.NotFound, Error("User id $candidate not found"))
                           else -> call.respond(users.first { it.id == call.parameters["id"]?.toLong() })
                       }
                   }
               }
               call.respond(users.first { it.id == call.parameters["id"]?.toLong() })

           }
           post {
               users.add(call.receive())
               call.respond("Added")
           }
           put{
               users[users.indexOf(call.receive())] = call.receive()
               call.respond("Updated")
           }
           delete { users.remove(call.receive()) }
       }
   }
}
