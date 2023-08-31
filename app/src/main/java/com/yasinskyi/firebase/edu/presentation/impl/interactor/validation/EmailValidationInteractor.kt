package com.yasinskyi.firebase.edu.presentation.impl.interactor.validation

import android.util.Patterns
import com.yasinskyi.firebase.edu.presentation.impl.exception.validation.EmailValidationException
import javax.inject.Inject

class EmailValidationInteractor @Inject constructor() {

    suspend operator fun invoke(email: String) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw EmailValidationException()
        }
    }
}