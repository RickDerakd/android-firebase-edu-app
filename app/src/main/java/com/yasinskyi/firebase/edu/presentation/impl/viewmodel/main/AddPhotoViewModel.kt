package com.yasinskyi.firebase.edu.presentation.impl.viewmodel.main

import com.yasinskyi.firebase.edu.R
import com.yasinskyi.firebase.edu.presentation.base.viewmodel.BaseViewModel
import com.yasinskyi.firebase.edu.presentation.impl.entity.PresentationDataEntity
import com.yasinskyi.firebase.edu.presentation.impl.interactor.UpdateUserInteractor
import com.yasinskyi.firebase.edu.presentation.impl.interactor.auth.SignOutInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AddPhotoViewModel @Inject constructor(
    presenterData: PresentationDataEntity,
    private val updateUserInteractor: UpdateUserInteractor,
    private val signOutInteractor: SignOutInteractor,
) : BaseViewModel(presenterData) {

    init {
        postTitle(R.string.app_name)
        postMenu(R.menu.menu_sign_out)
        postNavIcon(true)
    }

    private val _imageUriState = MutableStateFlow<String?>(null)
    val imageUriState: StateFlow<String?> = _imageUriState.asStateFlow()

    private val _signOutState = MutableStateFlow(false)
    val signOutState: StateFlow<Boolean> = _signOutState.asStateFlow()

    override fun onFailure(t: Throwable) {
        onLoading(false)
        super.onFailure(t)
    }

    fun setImageUri(uri: String?) {
        launchSafe {
            if (!loadingState.value) {
                onLoading(true)
                updateImageUriState(uri)
                onLoading(false)
            }
        }
    }

    fun updateUser() {
        launchSafe {
            if (!loadingState.value) {
                onLoading(true)
                updateUserInteractor(imageUriState.value)
                onLoading(false)
            }
        }
    }

    fun signOut() {
        launchSafe {
            onLoading(true)
            signOutInteractor()
            updateSignOutState(true)
            onLoading(false)
        }
    }

    private fun updateImageUriState(uri: String?) {
        launchSafe {
            _imageUriState.emit(uri)
        }
    }

    private fun updateSignOutState(isSuccessful: Boolean) {
        launchSafe {
            _signOutState.emit(isSuccessful)
        }
    }
}