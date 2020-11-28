package com.example.features.user.models

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object Users: Table() {
    val id = integer("id").autoIncrement()
    val name= varchar("name",255)
    val surname = varchar("surname", 255)
    override val primaryKey = PrimaryKey(id)
}

data class User(val id: Int, val name: String, val surname: String)