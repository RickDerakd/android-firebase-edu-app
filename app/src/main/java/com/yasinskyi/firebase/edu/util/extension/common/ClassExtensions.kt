package com.yasinskyi.firebase.edu.util.extension.common

import androidx.annotation.CheckResult
import androidx.annotation.IntRange
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

@CheckResult fun <C> Class<*>.getFunFromGenericClass(
    @IntRange(from = 0) genericPosition: Int,
    conditionForSearchFun: (Method) -> Boolean,
): Method = getFunFromGenericClassOrNull<C>(genericPosition, conditionForSearchFun)!!

@CheckResult fun <C> Class<*>.getFunFromGenericClassOrNull(
    @IntRange(from = 0) genericPosition: Int,
    conditionForSearchFun: (Method) -> Boolean,
): Method? =
    getGenericClass<C>(genericPosition)
        .declaredMethods
        .firstOrNull(conditionForSearchFun)
        ?: superclass?.getFunFromGenericClassOrNull<C>(genericPosition, conditionForSearchFun)

@CheckResult
fun <C> Class<*>.getGenericClass(@IntRange(from = 0) genericPosition: Int): Class<C> {
    val result = getGenericClassOrNull<C>(genericPosition)

    if (result == null) {
        val newGenericPosition = genericPosition - 1

        if (newGenericPosition == -1) throw NoSuchElementException()

        return getGenericClass(newGenericPosition)
    }

    return result
}

@CheckResult
@Suppress("UNCHECKED_CAST")
fun <C> Class<*>.getGenericClassOrNull(@IntRange(from = 0) genericPosition: Int): Class<C>? =
    ((genericSuperclass as? ParameterizedType?)
        ?.actualTypeArguments
        ?.getOrNull(genericPosition) as? Class<C>)
        ?: superclass?.getGenericClassOrNull(genericPosition)
