package com.example.todo2.analytics

import com.example.core.analytics.AnalyticsService
import io.appmetrica.analytics.AppMetrica
import javax.inject.Inject
import com.google.firebase.crashlytics.FirebaseCrashlytics

class AnalyticsServiceImpl @Inject constructor() : AnalyticsService {

    private val crashlytics = FirebaseCrashlytics.getInstance()

    override fun trackEvent(name: String, params: Map<String, Any>) {
        if (params.isEmpty()) {
            AppMetrica.reportEvent(name)
        } else {
            AppMetrica.reportEvent(name, params.mapValues { it.value.toString() })
        }
        AppMetrica.sendEventsBuffer()

        crashlytics.log("Event: $name, Params: $params")
    }

    override fun trackError(message: String, error: Throwable?) {
        AppMetrica.reportError(message, error)
        error?.let { crashlytics.recordException(it) } ?: crashlytics.log("Error: $message")
    }

    override fun log(message: String) {
        crashlytics.log(message)
        AppMetrica.reportEvent("Log: $message")
    }

    override fun setKey(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
        AppMetrica.putErrorEnvironmentValue(key, value)
    }

    override fun setUserId(userId: String?) {
        val id = userId.orEmpty()
        crashlytics.setUserId(id)
        AppMetrica.setUserProfileID(id)
    }

    override fun recordNonFatal(throwable: Throwable) {
        crashlytics.recordException(throwable)
        AppMetrica.reportError("non_fatal_error", throwable)
    }
}