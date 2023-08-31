package com.yasinskyi.firebase.edu.presentation.base.viewmodel

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

interface CoroutineExecutor {

    val scope: CoroutineScope

    val backgroundDispatcher: CoroutineDispatcher

    val coroutineExceptionHandler: CoroutineExceptionHandler

    val unexpectedErrorState: SharedFlow<Throwable>

    val loadingState: StateFlow<Boolean>

    fun launchSafe(
        context: CoroutineContext = backgroundDispatcher,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        onCancellation: (e: CancellationException) -> Unit = ::onCancellation,
        onFailure: (t: Throwable) -> Unit = this::onFailure,
        finally: (() -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit,
    ): Job =
        scope.launch(context + coroutineExceptionHandler, start) {
            try {
                block()
            } catch (e: CancellationException) {
                onCancellation(e)
            } catch (t: Throwable) {
                onFailure(t)
            } finally {
                finally?.invoke()
            }
        }

    fun onLoading(isLoading: Boolean)

    fun onFailure(t: Throwable)

    fun onCancellation(e: CancellationException) {
        // none
    }
}
