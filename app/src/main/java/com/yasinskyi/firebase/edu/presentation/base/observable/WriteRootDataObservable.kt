package com.yasinskyi.firebase.edu.presentation.base.observable

import com.yasinskyi.firebase.edu.view.impl.entity.RootDataUIEntity

interface WriteRootDataObservable : ReadRootDataObservable {

    fun post(newData: RootDataUIEntity): Boolean
}
