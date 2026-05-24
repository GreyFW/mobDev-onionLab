package com.example.presentation_auth

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.example.core.analytics.AnalyticsService
import com.example.domain_auth.AuthService
import com.example.domain_auth.models.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Authenticated(val userName: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService,
    private val analytics: AnalyticsService
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    init {
        checkSavedUser()
    }

    private fun checkSavedUser() {
        val user = authService.getCurrentUser()
        if (user != null) {
            _authState.value = AuthState.Authenticated(user.name)
        }
    }

    fun getLoginIntent(): Intent {
        return authService.getLoginIntent()
    }

    fun handleLoginResult(resultCode: Int, data: Intent?) {
        _authState.value = AuthState.Loading
        val result = authService.handleResult(resultCode, data)

        when (result) {
            is AuthResult.Success -> {
                analytics.trackEvent(
                    name = "user_logged_in",
                    params = mapOf("provider" to "yandex")
                )
                _authState.value = AuthState.Authenticated(result.user.name)
            }
            is AuthResult.Error -> {
                analytics.setKey("layer", "viewmodel")
                analytics.setKey("action", "login")
                analytics.recordNonFatal(Exception("Login error: ${result.message}"))

                _authState.value = AuthState.Error(result.message)
            }
            AuthResult.Cancelled -> {
                _authState.value = AuthState.Idle
            }
        }
    }

    fun logout() {
        authService.logout()
        _authState.value = AuthState.Idle
    }
}