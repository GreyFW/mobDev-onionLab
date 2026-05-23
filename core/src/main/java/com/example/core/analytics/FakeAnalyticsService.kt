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

    override fun log(message: String) { println("Log: $message") }
    override fun setKey(key: String, value: String) { println("Key: $key -> $value") }
    override fun setUserId(userId: String?) { println("UserID: $userId") }
    override fun recordNonFatal(throwable: Throwable) { println("NonFatal: ${throwable.message}") }
}