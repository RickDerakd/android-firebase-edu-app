package com.yasinskyi.firebase.edu.data

import com.yasinskyi.firebase.edu.presentation.impl.entity.Credentials

interface AuthRepository {

    suspend fun signIn(credentials: Credentials)

    suspend fun signOut()

    suspend fun createUser(credentials: Credentials)

    suspend fun resignIn()

    suspend fun sendPasswordResetEmail(email: String)
}