package com.yasinskyi.firebase.edu.presentation.impl.exception.validation

import com.yasinskyi.firebase.edu.presentation.impl.exception.ValidationException

class CredentialsValidationException(
    val exceptions: List<Throwable>,
) : ValidationException()
