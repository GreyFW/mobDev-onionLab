package com.example.todo2.domain.repositories

import com.example.todo2.domain.models.User

interface IAuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(email: String, password: String): Result<User>
    suspend fun logout(): Result<Unit>
}