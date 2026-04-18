package com.example.domain_auth

interface IAuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(email: String, password: String): Result<User>
    suspend fun logout(): Result<Unit>
}