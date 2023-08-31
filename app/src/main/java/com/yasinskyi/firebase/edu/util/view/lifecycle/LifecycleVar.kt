package com.yasinskyi.firebase.edu.util.view.lifecycle

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import com.yasinskyi.firebase.edu.util.extension.lifecycle.isDestroyed
import com.yasinskyi.firebase.edu.util.extension.lifecycle.launchOnLifecycleDestroy
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@CheckResult
@MainThread
@Suppress("unused")
fun <T> Delegates.lifecycle(
    lifecycleOwnerProvider: () -> LifecycleOwner,
    initializer: () -> T,
): ReadWriteProperty<Any, T?> =
    LifecycleVar(lifecycleOwnerProvider, initializer)

private class LifecycleVar<T>(
    private val lifecycleOwnerProvider: () -> LifecycleOwner,
    private val initializer: () -> T,
) : ReadWriteProperty<Any, T?> {

    private var value: T? = null

    override fun getValue(thisRef: Any, property: KProperty<*>): T? =
        if (value == null) {
            val lifecycleOwner = lifecycleOwnerProvider()

            if (lifecycleOwner.lifecycle.isDestroyed) {
                null
            } else {
                initializer.invoke()?.also { initializedValue ->
                    value = initializedValue
                    lifecycleOwner.launchOnLifecycleDestroy { value = null }
                }
            }
        } else {
            value
        }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
        this.value = value
    }
}
