package com.yasinskyi.firebase.edu.di.module.activity

import android.content.Context
import com.yasinskyi.firebase.edu.view.base.dialog.MessageInterface
import com.yasinskyi.firebase.edu.view.impl.dialog.alert.AlertMessageInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object MainActivityModule {

    @Provides @ActivityScoped
    fun provideMessageInterface(@ActivityContext context: Context): MessageInterface {
        return AlertMessageInterface(context)
    }
}