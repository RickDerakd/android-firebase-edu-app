package com.yasinskyi.firebase.edu.presentation.impl.interactor

import com.yasinskyi.firebase.edu.data.UserRepository
import com.yasinskyi.firebase.edu.presentation.impl.interactor.validation.ImageUrlValidationInteractor
import javax.inject.Inject

class UpdateUserInteractor @Inject constructor(
    private val imageUrlValidationInteractor: ImageUrlValidationInteractor,
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(imageUri: String?) {
        userRepository
            .updateUserImage(
                imageUrlValidationInteractor(imageUri)
            )
    }
}