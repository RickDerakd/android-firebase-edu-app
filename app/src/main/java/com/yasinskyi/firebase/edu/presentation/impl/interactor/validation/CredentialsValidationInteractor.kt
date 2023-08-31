package com.yasinskyi.firebase.edu.presentation.impl.interactor.validation

import com.yasinskyi.firebase.edu.presentation.impl.exception.validation.EmailValidationException
import com.yasinskyi.firebase.edu.presentation.impl.exception.validation.PasswordValidationException
import com.yasinskyi.firebase.edu.presentation.impl.entity.Credentials
import com.yasinskyi.firebase.edu.presentation.impl.exception.validation.CredentialsValidationException
import java.lang.Exception
import javax.inject.Inject

class CredentialsValidationInteractor @Inject constructor(
    val emailValidationInteractor: EmailValidationInteractor,
    val passwordValidationInteractor: PasswordValidationInteractor,
) {

    suspend operator fun invoke(credentials: Credentials) {
        val listException: MutableList<Exception> = mutableListOf()

        try {
            emailValidationInteractor(credentials.email)
        } catch (e: EmailValidationException) {
            listException.add(
                EmailValidationException()
            )
        }

        try {
            passwordValidationInteractor(credentials.password)
        } catch (e: PasswordValidationException) {
            listException.add(
                PasswordValidationException()
            )
        }

        if (listException.size > 0) {
            throw CredentialsValidationException(listException)
        }
    }
}