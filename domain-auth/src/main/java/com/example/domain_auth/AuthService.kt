package com.example.domain_auth

import android.content.Intent
import com.example.domain_auth.models.AuthResult
import com.example.domain_auth.models.User

interface AuthService {
    fun getLoginIntent(): Intent
    fun handleResult(resultCode: Int, data: Intent?): AuthResult
    fun logout()
    fun getCurrentUser(): User?
}