package com.yasinskyi.firebase.edu.data.exception

import java.io.IOException

class InternetConnectionException(
    message: String? = null,
    cause: Throwable? = null,
) : IOException(message, cause)