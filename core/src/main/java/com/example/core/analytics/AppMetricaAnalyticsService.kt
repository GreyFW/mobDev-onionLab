package com.example.core.analytics

import io.appmetrica.analytics.AppMetrica
import javax.inject.Inject

class AppMetricaAnalyticsService @Inject constructor() : AnalyticsService {

    override fun trackEvent(name: String, params: Map<String, Any>) {
        if (params.isEmpty()) {
            AppMetrica.reportEvent(name)
        } else {
            AppMetrica.reportEvent(name, params.mapValues { it.value.toString() })
        }
        AppMetrica.sendEventsBuffer()
    }

    override fun trackError(message: String, error: Throwable?) {
        AppMetrica.reportError(message, error)
    }
}