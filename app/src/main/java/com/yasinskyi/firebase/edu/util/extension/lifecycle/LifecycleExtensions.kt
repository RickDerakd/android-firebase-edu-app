package com.yasinskyi.firebase.edu.util.extension.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.yasinskyi.firebase.edu.util.extension.concurrent.awaitCancellation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

inline val Lifecycle.isDestroyed get() = currentState == Lifecycle.State.DESTROYED

inline fun Lifecycle.launchOnLifecycleCreate(
    crossinline block: CoroutineScope.() -> Unit,
): Job = launchOnLifecycleEvent(Lifecycle.Event.ON_CREATE) { block() }

inline fun Lifecycle.launchOnLifecycleStart(
    crossinline block: CoroutineScope.() -> Unit,
): Job = launchOnLifecycleEvent(Lifecycle.Event.ON_START) { block() }

inline fun Lifecycle.launchOnLifecycleResume(
    crossinline block: CoroutineScope.() -> Unit,
): Job = launchOnLifecycleEvent(Lifecycle.Event.ON_RESUME) { block() }

inline fun Lifecycle.launchOnLifecyclePause(
    crossinline block: CoroutineScope.() -> Unit,
): Job = launchOnLifecycleEvent(Lifecycle.Event.ON_PAUSE) { block() }

inline fun Lifecycle.launchOnLifecycleStop(
    crossinline block: CoroutineScope.() -> Unit,
): Job = launchOnLifecycleEvent(Lifecycle.Event.ON_STOP) { block() }

inline fun Lifecycle.launchOnLifecycleDestroy(
    crossinline block: CoroutineScope.() -> Unit,
): Job = launchOnLifecycleEvent(Lifecycle.Event.ON_DESTROY) { block() }

inline fun Lifecycle.launchOnLifecycle(
    crossinline block: CoroutineScope.(Lifecycle.Event) -> Unit,
): Job = launchOnLifecycleEvent(Lifecycle.Event.ON_ANY, block)

inline fun Lifecycle.launchOnLifecycleEvent(
    event: Lifecycle.Event,
    crossinline block: CoroutineScope.(Lifecycle.Event) -> Unit,
): Job = coroutineScope.launch {
    val lifecycleObserver = LifecycleEventObserver { _, newEvent ->
        if (event == Lifecycle.Event.ON_ANY || event == newEvent) {
            block(newEvent)
        }
    }

    this@launchOnLifecycleEvent.addObserver(lifecycleObserver)

    awaitCancellation {
        this@launchOnLifecycleEvent.removeObserver(lifecycleObserver)
    }
}

fun <T> LifecycleOwner.observeWhenCreated(producer: Flow<T>, receiver: (T) -> Unit) {
    lifecycleScope.launchWhenCreated {
        producer.collect {
            receiver(it)
        }
    }
}