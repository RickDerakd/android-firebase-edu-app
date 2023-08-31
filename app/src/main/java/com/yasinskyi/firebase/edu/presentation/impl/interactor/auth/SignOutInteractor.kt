package com.yasinskyi.firebase.edu.presentation.impl.interactor.auth

import com.yasinskyi.firebase.edu.data.AuthRepository
import javax.inject.Inject

class SignOutInteractor @Inject constructor(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke() {
        authRepository.signOut()
    }
}