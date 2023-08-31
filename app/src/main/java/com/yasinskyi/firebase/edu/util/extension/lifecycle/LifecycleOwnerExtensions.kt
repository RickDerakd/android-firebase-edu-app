package com.yasinskyi.firebase.edu.util.extension.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

inline fun LifecycleOwner.launchOnLifecycleCreate(
    crossinline block: CoroutineScope.() -> Unit,
): Job = lifecycle.launchOnLifecycleCreate { block() }

inline fun LifecycleOwner.launchOnLifecycleStart(
    crossinline block: CoroutineScope.() -> Unit,
): Job = lifecycle.launchOnLifecycleStart { block() }

inline fun LifecycleOwner.launchOnLifecycleResume(
    crossinline block: CoroutineScope.() -> Unit,
): Job = lifecycle.launchOnLifecycleResume { block() }

inline fun LifecycleOwner.launchOnLifecyclePause(
    crossinline block: CoroutineScope.() -> Unit,
): Job = lifecycle.launchOnLifecyclePause { block() }

inline fun LifecycleOwner.launchOnLifecycleStop(
    crossinline block: CoroutineScope.() -> Unit,
): Job = lifecycle.launchOnLifecycleStop { block() }

inline fun LifecycleOwner.launchOnLifecycleDestroy(
    crossinline block: CoroutineScope.() -> Unit,
): Job = lifecycle.launchOnLifecycleDestroy { block() }

inline fun LifecycleOwner.launchOnLifecycle(
    crossinline block: CoroutineScope.(Lifecycle.Event) -> Unit,
): Job = lifecycle.launchOnLifecycle { block(it) }

inline fun LifecycleOwner.launchOnLifecycleEvent(
    event: Lifecycle.Event,
    crossinline block: CoroutineScope.(Lifecycle.Event) -> Unit,
): Job = lifecycle.launchOnLifecycleEvent(event, block)