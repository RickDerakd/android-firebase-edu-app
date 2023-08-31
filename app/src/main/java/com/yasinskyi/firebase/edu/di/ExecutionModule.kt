package com.yasinskyi.firebase.edu.di

import com.yasinskyi.firebase.edu.di.qualifier.execution.Computation
import com.yasinskyi.firebase.edu.di.qualifier.execution.Foreground
import com.yasinskyi.firebase.edu.di.qualifier.execution.IO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class ExecutionModule {

    @Provides @Computation
    fun provideComputationDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides @IO
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides @Foreground
    fun provideForegroundDispatcher(): CoroutineDispatcher = Dispatchers.Main
}
