package com.example.core.analytics

interface AnalyticsService {
    fun trackEvent(name: String, params: Map<String, Any> = emptyMap())
    fun trackError(message: String, error: Throwable? = null)

    fun log(message: String)
    fun setKey(key: String, value: String)
    fun setUserId(userId: String?)
    fun recordNonFatal(throwable: Throwable)
}