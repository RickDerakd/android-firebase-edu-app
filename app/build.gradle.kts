import Versions.VERSION_CODE
import Versions.VERSION_NAME

plugins {
    id("com.android.application")

    kotlin("android")
    kotlin("kapt")

    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.yasinskyi.firebase.edu"

        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionName = VERSION_NAME
        versionCode = VERSION_CODE
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        targetCompatibility(libs.versions.jvmTarget.get().toInt())
        sourceCompatibility(libs.versions.jvmTarget.get().toInt())
    }

    kotlinOptions {
        jvmTarget = libs.versions.kotlinJvmTarget.get()
    }

    val compileSourcesTask = task("compileSources")

    applicationVariants.all {
        val flavoredCompileSourcesTask = tasks
            .named("compile${this@all.name.capitalize()}Sources")
            .get()
        compileSourcesTask.dependsOn(flavoredCompileSourcesTask)
    }
}

dependencies {
    // kotlin
    implementation(libs.bundles.kotlin)

    //// jetpack
    // jetpack foundation
    implementation(libs.bundles.jetpack.foundation)
    implementation(libs.bundles.jetpack.arch)
    implementation(libs.bundles.jetpack.ui)
    implementation(libs.bundles.views)

    // navigation
    implementation(libs.bundles.navigation)

    // di
    implementation(libs.bundles.hilt)
    kapt(libs.bundles.hilt.annotation)

    // firebase
    implementation(libs.bundles.firebase)
    implementation(libs.play.servises.auth)

    // glide
    implementation(libs.glide.core)
    kapt(libs.glide.compiler)

    // multithreading
    implementation(libs.bundles.multithreading)

    // utils
    implementation(libs.timber)

    debugImplementation(libs.leakcanaryDebug)
    implementation(libs.leakcanary)
}
