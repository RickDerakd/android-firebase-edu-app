package com.yasinskyi.firebase.edu.util.extension.concurrent

import androidx.annotation.CheckResult
import com.yasinskyi.firebase.edu.BuildConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber

@get:CheckResult val safeCoroutineExceptionHandler
    get(): CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            if (BuildConfig.DEBUG) {
                Thread.currentThread()
                    .uncaughtExceptionHandler
                    ?.uncaughtException(Thread.currentThread(), throwable)
            } else {
                Timber.e(throwable, "Unexpected error")
            }
        }

suspend inline fun awaitCancellation(block: () -> Unit) {
    try {
        kotlinx.coroutines.awaitCancellation()
    } catch (_: CancellationException) {
        block()
    } catch (throwable: Throwable) {
        Timber.e(throwable)
    }
}
