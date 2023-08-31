package com.yasinskyi.firebase.edu.di.module.viewmodel

import com.yasinskyi.firebase.edu.presentation.base.entity.PresentationDataDelegate
import com.yasinskyi.firebase.edu.presentation.base.observable.RootDataObservableAdapter
import com.yasinskyi.firebase.edu.presentation.impl.entity.PresentationDataEntity
import com.yasinskyi.firebase.edu.presentation.impl.observable.RootDataObservableAdapterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
interface BaseViewModelModule {

    @Binds
    fun bindPresentationDataDelegate(entity: PresentationDataEntity): PresentationDataDelegate

    @Binds
    fun bindRootDataObservableAdapter(
        adapter: RootDataObservableAdapterImpl,
    ): RootDataObservableAdapter
}
