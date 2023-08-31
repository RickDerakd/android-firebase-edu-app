package com.yasinskyi.firebase.edu.presentation.impl.interactor.validation

import com.yasinskyi.firebase.edu.presentation.impl.exception.validation.EmailValidationException
import com.yasinskyi.firebase.edu.presentation.impl.exception.validation.PasswordValidationException
import com.yasinskyi.firebase.edu.presentation.impl.exception.validation.UsernameValidationException
import com.yasinskyi.firebase.edu.presentation.impl.entity.User
import com.yasinskyi.firebase.edu.presentation.impl.entity.Credentials
import com.yasinskyi.firebase.edu.presentation.impl.exception.validation.RegistrationValidationException
import java.lang.Exception
import javax.inject.Inject

class RegistrationValidationInteractor @Inject constructor(
    val emailValidationInteractor: EmailValidationInteractor,
    val passwordValidationInteractor: PasswordValidationInteractor,
    val usernameValidationInteractor: UsernameValidationInteractor,
) {

    suspend operator fun invoke(credentials: Credentials, user: User) {
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

        try {
            usernameValidationInteractor(user.username)
        } catch (e: UsernameValidationException) {
            listException.add(
                UsernameValidationException()
            )
        }

        if (listException.size > 0) {
            throw RegistrationValidationException(listException)
        }
    }
}