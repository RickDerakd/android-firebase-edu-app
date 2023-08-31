package com.yasinskyi.firebase.edu.presentation.base.observable

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.yasinskyi.firebase.edu.view.impl.entity.RootDataUIEntity
import kotlin.reflect.KClass

interface RootDataObservable {

    fun <C : RootDataUIEntity> setDataConcater(
        key: KClass<out C>,
        concat: (C, C?) -> C = { old: C, new: C? -> new ?: old },
    )

    fun observe(owner: LifecycleOwner, observer: Observer<RootDataUIEntity>)
}
