package com.yasinskyi.firebase.edu.data.exception

import java.io.IOException

open class AuthException(
    message: String? = null,
    cause: Throwable? = null,
) : IOException(message, cause)