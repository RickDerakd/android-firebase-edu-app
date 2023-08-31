package com.yasinskyi.firebase.edu.presentation.base.observable

import com.yasinskyi.firebase.edu.view.impl.entity.RootDataUIEntity
import kotlin.reflect.KClass

interface ReadRootDataObservable : RootDataObservable {

    fun <T : RootDataUIEntity> get(key: KClass<out T>): T?
}
