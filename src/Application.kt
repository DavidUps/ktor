package com.example

import com.example.core.database.DatabaseFactory
import com.example.core.router.userRout
import com.example.features.user.services.UserService
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.gson.*
import io.ktor.features.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
        }
    }

    DatabaseFactory.init()

    val userService = UserService()

    install(Routing) {
        userRout(userService)
    }
}