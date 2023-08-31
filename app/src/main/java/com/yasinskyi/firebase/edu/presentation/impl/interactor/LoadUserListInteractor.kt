package com.yasinskyi.firebase.edu.presentation.impl.interactor

import com.yasinskyi.firebase.edu.data.UserRepository
import javax.inject.Inject

class LoadUserListInteractor @Inject constructor(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(limit: Int) =
        userRepository
            .loadUsers(limit)
}