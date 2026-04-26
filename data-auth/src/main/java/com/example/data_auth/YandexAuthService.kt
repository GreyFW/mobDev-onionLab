package com.example.data_auth

import android.content.Context
import android.content.Intent
import com.example.domain_auth.AuthService
import com.example.domain_auth.models.AuthResult
import com.example.domain_auth.models.User
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthResult
import com.yandex.authsdk.YandexAuthSdk
import javax.inject.Inject

class YandexAuthService @Inject constructor(
    private val context: Context,
    private val tokenManager: TokenManager
) : AuthService {

    private val sdk = YandexAuthSdk.create(YandexAuthOptions(context))

    override fun getLoginIntent(): Intent {
        val loginOptions = YandexAuthLoginOptions()

        return sdk.contract.createIntent(context, loginOptions)
    }

    override fun handleResult(resultCode: Int, data: Intent?): AuthResult {
        val yandexResult = sdk.contract.parseResult(resultCode, data)

        return when (yandexResult) {
            is YandexAuthResult.Success -> {
                val yandexTokenValue = yandexResult.token.value
                tokenManager.saveUserData("yandex_id", "Yandex User", yandexTokenValue)
                AuthResult.Success(User("yandex_id", "Yandex User", null, null))
            }
            is YandexAuthResult.Cancelled -> {
                AuthResult.Cancelled
            }
            is YandexAuthResult.Failure -> {
                AuthResult.Error(yandexResult.exception.message ?: "Yandex auth failed")
            }
        }
    }

    override fun logout() {
        tokenManager.clear()
    }

    override fun getCurrentUser(): User? {
        val name = tokenManager.getUserName()
        return if (name != null) User("saved_id", name, null, null) else null
    }
}