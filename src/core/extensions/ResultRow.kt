package com.example.core.extensions

import com.example.features.user.models.User
import com.example.features.user.models.Users
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toUsers(row: ResultRow): User {
    return User(
        row[Users.id],
        row[Users.name],
        row[Users.surname]
    )
}