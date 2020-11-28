package com.example.features.user.services

import com.example.core.extensions.dbQuery
import com.example.core.extensions.toUsers
import com.example.features.user.models.User
import com.example.features.user.models.Users
import org.jetbrains.exposed.sql.*

class UserService {

    suspend fun getAllUsers(): List<User>? = dbQuery {
        Users.selectAll().map { it.toUsers(it) }
    }

    suspend fun getUser(id: Int): User? = dbQuery {
        Users.select {
            (Users.id eq id)
        }.mapNotNull {
            it.toUsers(it)
        }.singleOrNull()
    }

    suspend fun postUser(user: User): User? {
        var key = 0
        dbQuery {
            key = (Users.insert {
                it[name] = user.name
                it[surname] = user.surname
            } get Users.id)
        }
        return getUser(key)
    }

    suspend fun updateUser(user: User): User? {
        val id = user.id
        return if (id == null) {
            postUser(user)
        } else {
            dbQuery {
                Users.update({ Users.id eq id }) {
                    it[name] = user.name
                    it[surname] = user.surname
                }
            }
            getUser(id)
        }
    }

    suspend fun deleteUser(id: Int): Boolean {
        return dbQuery {
            Users.deleteWhere { Users.id eq id } > 0
        }
    }
}