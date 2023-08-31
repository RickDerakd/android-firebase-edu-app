package com.yasinskyi.firebase.edu.presentation.impl.viewmodel.main

import com.yasinskyi.firebase.edu.R
import com.yasinskyi.firebase.edu.presentation.base.viewmodel.BaseViewModel
import com.yasinskyi.firebase.edu.presentation.impl.entity.PresentationDataEntity
import com.yasinskyi.firebase.edu.presentation.impl.entity.User
import com.yasinskyi.firebase.edu.presentation.impl.interactor.LoadUserListInteractor
import com.yasinskyi.firebase.edu.presentation.impl.interactor.auth.SignOutInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    presenterData: PresentationDataEntity,
    private val loadUserListInteractor: LoadUserListInteractor,
    private val signOutInteractor: SignOutInteractor,
) : BaseViewModel(presenterData) {

    init {
        postTitle(R.string.app_name)
        postMenu(R.menu.menu_home)
        postNavIcon(false)
    }

    private val _userListState = MutableStateFlow<List<User>?>(null)
    val userListState: StateFlow<List<User>?> = _userListState.asStateFlow()

    private val _signOutState = MutableStateFlow(false)
    val signOutState: StateFlow<Boolean> = _signOutState.asStateFlow()

    private fun updateUserListState(userList: List<User>?) {
        launchSafe {
            _userListState.emit(userList)
        }
    }

    private fun updateSignOutState(isSuccessful: Boolean) {
        launchSafe {
            _signOutState.emit(isSuccessful)
        }
    }

    override fun onFailure(t: Throwable) {
        onLoading(false)
        super.onFailure(t)
    }

    fun loadUsers() {
        launchSafe {
            if (!loadingState.value) {
                onLoading(true)
                loadUserListInteractor(
                    (userListState.value?.size ?: 0) + PAGE_SIZE,
                ).collect { userList ->
                    updateUserListState(userList)
                    onLoading(false)
                }
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

    companion object {

        const val PAGE_SIZE = 15
    }
}