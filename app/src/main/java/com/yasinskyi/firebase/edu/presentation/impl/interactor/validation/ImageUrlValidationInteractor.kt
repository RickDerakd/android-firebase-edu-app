package com.yasinskyi.firebase.edu.presentation.impl.interactor.validation

import com.yasinskyi.firebase.edu.presentation.impl.exception.validation.ImageUriValidationException
import javax.inject.Inject

class ImageUrlValidationInteractor @Inject constructor() {

    suspend operator fun invoke(imageUri: String?): String {
        if (imageUri.isNullOrEmpty()) {
            throw ImageUriValidationException()
        } else {
            return imageUri
        }
    }
}