package com.yasinskyi.firebase.edu.presentation.base.entity

import com.yasinskyi.firebase.edu.presentation.base.observable.RootDataObservableAdapter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler

interface PresentationDataDelegate {

    val backgroundDispatcher: CoroutineDispatcher

    val coroutineExceptionHandler: CoroutineExceptionHandler

    val rootDataObservable: RootDataObservableAdapter
}
