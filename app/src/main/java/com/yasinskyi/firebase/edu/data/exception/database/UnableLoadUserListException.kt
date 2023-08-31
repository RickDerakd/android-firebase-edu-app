package com.yasinskyi.firebase.edu.data.exception.database

import java.io.IOException

class UnableLoadUserListException(
    message: String? = null,
    cause: Throwable? = null,
) : IOException(message, cause)