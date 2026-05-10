import java.util.Properties

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) load(file.inputStream())
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    id("com.google.gms.google-services")
}

configurations.all {
    resolutionStrategy {
        force("com.squareup:javapoet:1.13.0")
    }
}

android {
    namespace = "com.example.todo2"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.todo2"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "APPMETRICA_API_KEY",
            "\"${localProperties["appmetrica_api_key"]}\""
        )
        buildConfigField(
            "String",
            "YANDEX_CLIENT_ID",
            "\"${localProperties["yandex_client_id"]}\""
        )
        manifestPlaceholders["YANDEX_CLIENT_ID"] = localProperties["yandex_client_id"].toString()
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core:navigation"))

    implementation(project(":domain-auth"))
    implementation(project(":data-auth"))
    implementation(project(":presentation-auth"))

    implementation(project(":domain-todo"))
    implementation(project(":data-todo"))
    implementation(project(":presentation-todo"))

    implementation(project(":presentation-about"))

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    ksp("com.squareup:javapoet:1.13.0")

    implementation("com.yandex.android:maps.mobile:4.4.0-lite")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    testImplementation(libs.konsist)
    testImplementation(libs.junit)

    implementation("io.appmetrica.analytics:analytics:8.1.0")

    implementation("com.yandex.android:authsdk:3.1.0")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    implementation(platform("com.google.firebase:firebase-bom:34.13.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-config")
    implementation("com.google.firebase:firebase-firestore")
}