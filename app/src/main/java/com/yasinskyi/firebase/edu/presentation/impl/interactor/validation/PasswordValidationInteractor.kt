package com.yasinskyi.firebase.edu.presentation.impl.interactor.validation

import com.yasinskyi.firebase.edu.presentation.impl.exception.validation.PasswordValidationException
import javax.inject.Inject

class PasswordValidationInteractor @Inject constructor() {

    suspend operator fun invoke(password: String?) {
        if (password.isNullOrEmpty()) {
            throw PasswordValidationException()
        }
    }
}