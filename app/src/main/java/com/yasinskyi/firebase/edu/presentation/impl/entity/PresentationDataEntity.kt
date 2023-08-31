package com.yasinskyi.firebase.edu.presentation.impl.entity

import com.yasinskyi.firebase.edu.di.qualifier.execution.Computation
import com.yasinskyi.firebase.edu.presentation.base.entity.PresentationDataDelegate
import com.yasinskyi.firebase.edu.presentation.base.observable.RootDataObservableAdapter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Inject

open class PresentationDataEntity @Inject constructor(
    @Computation override val backgroundDispatcher: CoroutineDispatcher,
    override val coroutineExceptionHandler: CoroutineExceptionHandler,
    override val rootDataObservable: RootDataObservableAdapter,
) : PresentationDataDelegate
