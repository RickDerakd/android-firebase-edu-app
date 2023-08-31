package com.yasinskyi.firebase.edu.presentation.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinskyi.firebase.edu.presentation.base.entity.PresentationDataDelegate
import com.yasinskyi.firebase.edu.presentation.base.observable.RootDataObservableAdapter
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel(
    presentationData: PresentationDataDelegate,
) : ViewModel(),
    PresentationDataDelegate by presentationData,
    RootDataObservableAdapter by presentationData.rootDataObservable,
    CoroutineExecutor {

    override val scope get() = viewModelScope

    private val _loadingState = MutableStateFlow(false)
    override val loadingState: StateFlow<Boolean> = _loadingState.asStateFlow()

    private val _unexpectedErrorState = MutableSharedFlow<Throwable>()
    override val unexpectedErrorState = _unexpectedErrorState.asSharedFlow()

    override fun onLoading(isLoading: Boolean) {
        launchSafe {
            _loadingState.emit(isLoading)
        }
    }

    override fun onFailure(t: Throwable) {
        launchSafe {
            _unexpectedErrorState.emit(t)
        }
    }
}
