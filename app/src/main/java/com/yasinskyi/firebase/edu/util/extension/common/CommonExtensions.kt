package com.yasinskyi.firebase.edu.util.extension.common

import androidx.annotation.CheckResult
import timber.log.Timber

@CheckResult fun <T> safeLazy(initializer: () -> T): Lazy<T> =
    lazy(LazyThreadSafetyMode.SYNCHRONIZED, initializer)

@CheckResult fun <T> unsafeLazy(initializer: () -> T): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE, initializer)

@CheckResult inline fun <T> tryOrNull(
    log: (Throwable?) -> Unit = Timber.Forest::w,
    block: () -> T,
): T? = runCatching(block).onFailure(log).getOrNull()
