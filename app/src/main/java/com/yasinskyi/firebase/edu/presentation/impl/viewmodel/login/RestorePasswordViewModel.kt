package com.yasinskyi.firebase.edu.presentation.impl.viewmodel.login

import com.yasinskyi.firebase.edu.R
import com.yasinskyi.firebase.edu.presentation.base.viewmodel.BaseViewModel
import com.yasinskyi.firebase.edu.presentation.impl.entity.PresentationDataEntity
import com.yasinskyi.firebase.edu.presentation.impl.interactor.auth.RestorePasswordInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RestorePasswordViewModel @Inject constructor(
    presenterData: PresentationDataEntity,
    private val restorePasswordInteractor: RestorePasswordInteractor,
) : BaseViewModel(presenterData) {

    init {
        postTitle(R.string.app_name)
        postMenu()
    }

    private val _emailFieldState = MutableStateFlow("")
    val emailFieldState: StateFlow<String> = _emailFieldState.asStateFlow()

    private val _restoreState = MutableStateFlow(false)
    val restoreState: StateFlow<Boolean> = _restoreState.asStateFlow()

    private fun updateEmailFieldStateState(email: String) {
        launchSafe {
            _emailFieldState.emit(email)
        }
    }

    private fun updateRestoreState(isSuccessful: Boolean) {
        launchSafe {
            _restoreState.emit(isSuccessful)
        }
    }

    override fun onFailure(t: Throwable) {
        onLoading(false)
        super.onFailure(t)
    }

    fun restorePassword() {
        launchSafe {
            if (!loadingState.value) {
                onLoading(true)
                restorePasswordInteractor(emailFieldState.value)
                updateRestoreState(true)
                onLoading(false)
            }
        }
    }

    fun saveFields(email: String) {
        updateEmailFieldStateState(email)
    }
}