package com.example.core.analytics

class FakeAnalyticsService : AnalyticsService {
    val loggedEvents = mutableListOf<Pair<String, Map<String, Any>>>()

    override fun trackEvent(name: String, params: Map<String, Any>) {
        loggedEvents.add(name to params)
        println("Tracked event: $name with params: $params")
    }

    override fun trackError(message: String, error: Throwable?) {
        println("Tracked error: $message")
    }
}