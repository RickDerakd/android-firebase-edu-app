package com.yasinskyi.firebase.edu.presentation.impl.viewmodel.login

import com.yasinskyi.firebase.edu.R
import com.yasinskyi.firebase.edu.presentation.base.viewmodel.BaseViewModel
import com.yasinskyi.firebase.edu.presentation.impl.entity.PresentationDataEntity
import com.yasinskyi.firebase.edu.presentation.impl.entity.Credentials
import com.yasinskyi.firebase.edu.presentation.impl.interactor.auth.SignInInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    presenterData: PresentationDataEntity,
    private val signInInteractor: SignInInteractor,
) : BaseViewModel(presenterData) {

    init {
        postTitle(R.string.app_name)
        postMenu()
        postNavIcon(false)
    }

    private val _emailFieldState = MutableStateFlow("")
    val emailFieldState: StateFlow<String> = _emailFieldState.asStateFlow()

    private val _passwordFieldState = MutableStateFlow("")
    val passwordFieldState: StateFlow<String> = _passwordFieldState.asStateFlow()

    private val _authState = MutableStateFlow(false)
    val authState: StateFlow<Boolean> = _authState.asStateFlow()

    private fun updateEmailFieldStateState(email: String) {
        launchSafe {
            _emailFieldState.emit(email)
        }
    }

    private fun updatePasswordFieldStateState(password: String) {
        launchSafe {
            _passwordFieldState.emit(password)
        }
    }

    private fun updateAuthState(isSuccessful: Boolean) {
        launchSafe {
            _authState.emit(isSuccessful)
        }
    }

    override fun onFailure(t: Throwable) {
        onLoading(false)
        super.onFailure(t)
    }

    fun signIn() {
        launchSafe {
            if (!loadingState.value) {
                onLoading(true)
                signInInteractor(
                    Credentials(
                        emailFieldState.value,
                        passwordFieldState.value,
                    )
                )
                updateAuthState(true)
                onLoading(false)
            }
        }
    }

    fun saveFields(email: String, password: String) {
        updateEmailFieldStateState(email)
        updatePasswordFieldStateState(password)
    }
}

