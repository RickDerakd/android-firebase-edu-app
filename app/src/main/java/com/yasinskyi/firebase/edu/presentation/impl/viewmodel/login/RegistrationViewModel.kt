package com.yasinskyi.firebase.edu.presentation.impl.viewmodel.login

import com.yasinskyi.firebase.edu.R
import com.yasinskyi.firebase.edu.presentation.base.viewmodel.BaseViewModel
import com.yasinskyi.firebase.edu.presentation.impl.entity.User
import com.yasinskyi.firebase.edu.presentation.impl.entity.PresentationDataEntity
import com.yasinskyi.firebase.edu.presentation.impl.entity.Credentials
import com.yasinskyi.firebase.edu.presentation.impl.interactor.auth.RegistrationInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    presenterData: PresentationDataEntity,
    val registrationInteractor: RegistrationInteractor,
) : BaseViewModel(presenterData) {

    init {
        postTitle(R.string.app_name)
        postMenu()
    }

    private val _emailFieldState = MutableStateFlow("")
    val emailFieldState: StateFlow<String> = _emailFieldState.asStateFlow()

    private val _passwordFieldState = MutableStateFlow("")
    val passwordFieldState: StateFlow<String> = _passwordFieldState.asStateFlow()

    private val _usernameFieldState = MutableStateFlow("")
    val usernameFieldState: StateFlow<String> = _usernameFieldState.asStateFlow()

    private val _registrationState = MutableStateFlow(false)
    val registrationState: StateFlow<Boolean> = _registrationState.asStateFlow()

    private val _imageUriState = MutableStateFlow<String?>(null)
    val imageUriState: StateFlow<String?> = _imageUriState.asStateFlow()

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

    private fun updateUsernameFieldStateState(username: String) {
        launchSafe {
            _usernameFieldState.emit(username)
        }
    }

    private fun updateRegistrationState(isSuccessful: Boolean) {
        launchSafe {
            _registrationState.emit(isSuccessful)
        }
    }

    private fun updateImageUriState(uri: String?) {
        launchSafe {
            _imageUriState.emit(uri)
        }
    }

    override fun onFailure(t: Throwable) {
        onLoading(false)
        super.onFailure(t)
    }

    fun createUser() {
        launchSafe {
            if (!loadingState.value) {
                onLoading(true)
                registrationInteractor(
                    Credentials(
                        emailFieldState.value,
                        passwordFieldState.value,
                    ),
                    User(
                        username = usernameFieldState.value,
                        imageUri = imageUriState.value,
                    ),
                )
                updateRegistrationState(true)
                onLoading(false)
            }
        }
    }

    fun setImageUri(uri: String?) {
        launchSafe {
            onLoading(true)
            updateImageUriState(uri)
            onLoading(false)
        }
    }

    fun saveFields(email: String, password: String, username: String) {
        updateUsernameFieldStateState(username)
        updateEmailFieldStateState(email)
        updatePasswordFieldStateState(password)
    }
}