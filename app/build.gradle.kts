plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
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

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}