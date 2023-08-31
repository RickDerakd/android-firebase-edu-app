package com.yasinskyi.firebase.edu.presentation.impl.exception.validation

import com.yasinskyi.firebase.edu.presentation.impl.exception.ValidationException

class RegistrationValidationException(
    val exceptions: List<Throwable>,
) : ValidationException()