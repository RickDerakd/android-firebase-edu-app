package com.yasinskyi.firebase.edu.data.exception.storage

import java.io.IOException

class UnableToUploadPhotoToStorageException(
    message: String? = null,
    cause: Throwable? = null,
) : IOException(message, cause)