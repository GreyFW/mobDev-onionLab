package com.example.domain_auth.models

sealed class AuthResult {
    data class Success(val user: User) : AuthResult()
    data class Error(val message: String) : AuthResult()
    object Cancelled : AuthResult()
}
