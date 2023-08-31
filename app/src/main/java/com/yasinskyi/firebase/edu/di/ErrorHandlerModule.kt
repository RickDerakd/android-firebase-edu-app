package com.yasinskyi.firebase.edu.di

import com.yasinskyi.firebase.edu.util.extension.concurrent.safeCoroutineExceptionHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ErrorHandlerModule {

    @Provides @Singleton
    fun provideSafeCoroutineExceptionHandler(): CoroutineExceptionHandler =
        safeCoroutineExceptionHandler
}
