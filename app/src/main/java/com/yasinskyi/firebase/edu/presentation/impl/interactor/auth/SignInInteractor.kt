package com.yasinskyi.firebase.edu.presentation.impl.interactor.auth

import com.yasinskyi.firebase.edu.data.AuthRepository
import com.yasinskyi.firebase.edu.presentation.impl.entity.Credentials
import com.yasinskyi.firebase.edu.presentation.impl.interactor.validation.CredentialsValidationInteractor
import javax.inject.Inject

class SignInInteractor @Inject constructor(
    private val credentialsValidationInteractor: CredentialsValidationInteractor,
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(credentials: Credentials) {
        credentialsValidationInteractor(credentials)
        authRepository.signIn(credentials)
    }
}