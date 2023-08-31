package com.yasinskyi.firebase.edu.util.logger

import timber.log.Timber

class DebugTree : Timber.DebugTree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, "Timber.$tag", message, t)
    }
}
