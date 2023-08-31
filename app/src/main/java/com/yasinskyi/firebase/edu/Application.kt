package com.yasinskyi.firebase.edu

import android.app.Application
import com.yasinskyi.firebase.edu.util.logger.DebugTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        setupTimber()
    }

    private fun setupTimber() {
        Timber.plant(DebugTree())
    }
}