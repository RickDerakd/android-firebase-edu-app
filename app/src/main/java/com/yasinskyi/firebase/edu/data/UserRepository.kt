package com.yasinskyi.firebase.edu.data

import com.yasinskyi.firebase.edu.presentation.impl.entity.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun loadUsers(limit: Int): Flow<List<User>?>

    suspend fun addUser(user: User)

    suspend fun updateUserImage(imageUri: String)
}