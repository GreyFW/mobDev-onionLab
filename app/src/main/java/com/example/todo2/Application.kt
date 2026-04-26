package com.example.todo2

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initAppMetrica()
    }

    private fun initAppMetrica() {
        val config = AppMetricaConfig
            .newConfigBuilder(BuildConfig.APPMETRICA_API_KEY)
            .withLogs()
            .withSessionTimeout(60)
            .build()

        AppMetrica.activate(this, config)

        AppMetrica.enableActivityAutoTracking(this)
    }
}