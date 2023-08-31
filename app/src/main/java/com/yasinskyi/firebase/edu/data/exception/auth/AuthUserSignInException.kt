package com.yasinskyi.firebase.edu.data.exception.auth

import com.yasinskyi.firebase.edu.data.exception.AuthException

class AuthUserSignInException(
    message: String? = null,
    cause: Throwable? = null,
) : AuthException(message, cause)