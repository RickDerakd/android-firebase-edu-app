package com.yasinskyi.firebase.edu.presentation.impl.exception.validation

import com.yasinskyi.firebase.edu.presentation.impl.exception.ValidationException

class ImageUriValidationException(
    message: String? = null,
    cause: Throwable? = null,
) : ValidationException(message, cause)