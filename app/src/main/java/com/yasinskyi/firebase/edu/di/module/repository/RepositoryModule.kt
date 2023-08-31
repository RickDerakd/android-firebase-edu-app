package com.yasinskyi.firebase.edu.di.module.repository

import com.yasinskyi.firebase.edu.data.AuthRepository
import com.yasinskyi.firebase.edu.data.UserRepository
import com.yasinskyi.firebase.edu.data.repository.FirebaseAuthRepository
import com.yasinskyi.firebase.edu.data.repository.FirebaseUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds @Singleton
    fun bindAuthRepository(firebaseAuthRepository: FirebaseAuthRepository): AuthRepository

    @Binds @Singleton
    fun bindUserRepository(firebaseUserRepository: FirebaseUserRepository): UserRepository
}