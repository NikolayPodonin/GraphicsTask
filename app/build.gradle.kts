plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.podonin.graphicstask"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.podonin.graphicstask"
        minSdk = 24
        targetSdk = 35
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
        viewBinding = true
    }
}

dependencies {

    implementation(project(":common-network"))
    implementation(project(":xygraph-api"))
    implementation(project(":xygraph-impl"))
    implementation(project(":photo-saver-api"))
    implementation(project(":photo-saver-impl"))
    implementation(project(":points-count-api"))
    implementation(project(":points-count-impl"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.cicerone)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}