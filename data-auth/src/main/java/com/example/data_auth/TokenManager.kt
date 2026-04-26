package com.example.data_auth

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(@ApplicationContext context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveUserData(id: String, name: String, token: String) {
        prefs.edit().apply {
            putString("user_id", id)
            putString("user_name", name)
            putString("access_token", token)
            apply()
        }
    }

    fun getUserName(): String? = prefs.getString("user_name", null)
    fun getToken(): String? = prefs.getString("access_token", null)

    fun clear() {
        prefs.edit().clear().apply()
    }
}