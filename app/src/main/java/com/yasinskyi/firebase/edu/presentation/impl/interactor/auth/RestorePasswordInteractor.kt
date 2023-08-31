package com.yasinskyi.firebase.edu.presentation.impl.interactor.auth

import com.yasinskyi.firebase.edu.data.AuthRepository
import com.yasinskyi.firebase.edu.presentation.impl.interactor.validation.EmailValidationInteractor
import javax.inject.Inject

class RestorePasswordInteractor @Inject constructor(
    val emailValidationInteractor: EmailValidationInteractor,
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(email: String) {
        emailValidationInteractor(email)
        authRepository.sendPasswordResetEmail(email)
    }
}