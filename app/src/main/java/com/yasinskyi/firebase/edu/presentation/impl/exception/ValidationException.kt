package com.yasinskyi.firebase.edu.presentation.impl.exception

open class ValidationException(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause)