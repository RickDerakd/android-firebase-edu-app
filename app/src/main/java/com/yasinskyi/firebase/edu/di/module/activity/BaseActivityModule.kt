package com.yasinskyi.firebase.edu.di.module.activity

import com.yasinskyi.firebase.edu.presentation.base.observable.ReadRootDataObservable
import com.yasinskyi.firebase.edu.presentation.base.observable.RootDataObservable
import com.yasinskyi.firebase.edu.presentation.base.observable.WriteRootDataObservable
import com.yasinskyi.firebase.edu.presentation.impl.observable.RootDataObservableImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
interface BaseActivityModule {

    @Binds @ActivityRetainedScoped
    fun bindWriteRootDataObservable(observable: RootDataObservableImpl): WriteRootDataObservable

    @Binds @ActivityRetainedScoped
    fun bindReadRootDataObservable(observable: WriteRootDataObservable): ReadRootDataObservable

    @Binds @ActivityRetainedScoped
    fun bindRootDataObservable(observable: ReadRootDataObservable): RootDataObservable
}