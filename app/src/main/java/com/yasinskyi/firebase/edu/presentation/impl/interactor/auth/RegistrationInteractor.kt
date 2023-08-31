package com.yasinskyi.firebase.edu.presentation.impl.interactor.auth

import com.yasinskyi.firebase.edu.data.AuthRepository
import com.yasinskyi.firebase.edu.data.UserRepository
import com.yasinskyi.firebase.edu.presentation.impl.entity.Credentials
import com.yasinskyi.firebase.edu.presentation.impl.entity.User
import com.yasinskyi.firebase.edu.presentation.impl.interactor.validation.RegistrationValidationInteractor
import javax.inject.Inject

class RegistrationInteractor @Inject constructor(
    private val registrationValidationInteractor: RegistrationValidationInteractor,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(credentials: Credentials, user: User) {
        registrationValidationInteractor(credentials, user)
        authRepository.createUser(credentials)
        userRepository.addUser(user)
    }
}