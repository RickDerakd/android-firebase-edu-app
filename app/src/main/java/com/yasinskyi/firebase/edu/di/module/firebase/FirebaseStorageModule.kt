package com.yasinskyi.firebase.edu.di.module.firebase

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseStorageModule {

    @Provides @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = Firebase.storage
}