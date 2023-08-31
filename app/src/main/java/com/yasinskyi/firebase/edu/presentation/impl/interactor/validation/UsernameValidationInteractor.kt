package com.yasinskyi.firebase.edu.presentation.impl.interactor.validation

import com.yasinskyi.firebase.edu.presentation.impl.exception.validation.UsernameValidationException
import com.yasinskyi.firebase.edu.util.matcher.UsernameMatcher
import javax.inject.Inject

class UsernameValidationInteractor @Inject constructor() {

    suspend operator fun invoke(username: String?) {
        if (username.isNullOrEmpty() || !UsernameMatcher.isUsername(username)) {
            throw UsernameValidationException()
        }
    }
}