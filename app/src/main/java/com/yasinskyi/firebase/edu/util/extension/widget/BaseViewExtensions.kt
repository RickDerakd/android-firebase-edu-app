package com.yasinskyi.firebase.edu.util.extension.widget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CheckResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import com.yasinskyi.firebase.edu.presentation.base.viewmodel.BaseViewModel
import com.yasinskyi.firebase.edu.util.extension.common.getFunFromGenericClass
import com.yasinskyi.firebase.edu.util.extension.common.getGenericClass
import com.yasinskyi.firebase.edu.view.BaseView
import java.lang.reflect.Method
import java.lang.reflect.Modifier

@CheckResult
@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> BaseView<VB, *>.inflateBinding(layoutInflater: LayoutInflater): VB =
    javaClass
        .getFunFromGenericClass<VB>(0, ::isInflateWithoutParentFun)
        .invoke(null, layoutInflater) as VB

@CheckResult
@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> BaseView<VB, *>.inflateBinding(
    layoutInflater: LayoutInflater,
    parent: ViewGroup?,
    attachToParent: Boolean,
): VB = javaClass
    .getFunFromGenericClass<VB>(0, ::isInflateWithParentFun)
    .invoke(null, layoutInflater, parent, attachToParent) as VB

@CheckResult private fun isInflateWithoutParentFun(method: Method): Boolean = method.run {
    Modifier.isStatic(modifiers) &&
            parameterTypes.size == 1 &&
            parameterTypes.first() == LayoutInflater::class.java
}

@CheckResult private fun isInflateWithParentFun(method: Method): Boolean = method.run {
    Modifier.isStatic(modifiers) &&
            parameterTypes.size == 3 &&
            parameterTypes[0] == LayoutInflater::class.java &&
            parameterTypes[1] == ViewGroup::class.java &&
            parameterTypes[2] == Boolean::class.java
}

@CheckResult fun <VM, SO> SO.createViewModel(): VM where
        VM : BaseViewModel,
        SO : BaseView<*, VM>,
        SO : ViewModelStoreOwner =
    ViewModelProvider(
        owner = this
    )[javaClass.getGenericClass(1)]
