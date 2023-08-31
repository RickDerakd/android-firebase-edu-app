package com.yasinskyi.firebase.edu.presentation.impl.observable

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.distinctUntilChanged
import com.yasinskyi.firebase.edu.presentation.base.observable.WriteRootDataObservable
import com.yasinskyi.firebase.edu.view.impl.entity.RootDataUIEntity
import timber.log.Timber
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName

class RootDataObservableImpl @Inject constructor() : WriteRootDataObservable {

    @VisibleForTesting val rootDataMap: Map<
            KClass<out RootDataUIEntity>,
            RootDataValue<RootDataUIEntity>,
            > = RootDataUIEntity::class
        .sealedSubclasses
        .asSequence()
        .map { it to RootDataValue<RootDataUIEntity>() }
        .toMap()

    override fun observe(owner: LifecycleOwner, observer: Observer<RootDataUIEntity>) {
        rootDataMap.values.forEach { it.observe(owner, observer) }
    }

    override fun post(newData: RootDataUIEntity): Boolean {
        val rootDataValue = rootDataMap[newData::class]
            ?: throw IllegalArgumentException("Key ${newData::class} not found")

        val oldData = rootDataValue.oldData
        var posted = true

        when {
            oldData == null -> rootDataValue.postValue(newData)

            newData != oldData -> {
                val newDataAfterConcater = rootDataValue.concat(oldData, newData)
                if (newDataAfterConcater == oldData) {
                    posted = false
                } else {
                    rootDataValue.postValue(newDataAfterConcater)
                }
            }

            else -> posted = false
        }

        if (!posted) {
            Timber.i("Data $newData already exists and equals to $oldData")
        }

        return posted
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : RootDataUIEntity> get(key: KClass<out T>): T? =
        rootDataMap[key]?.oldData as T?

    override fun <C : RootDataUIEntity> setDataConcater(
        key: KClass<out C>,
        concat: (C, C?) -> C,
    ) {
        val rootData = rootDataMap[key] ?: throw IllegalArgumentException(key.jvmName)
        @Suppress("UNCHECKED_CAST")
        rootData.concat = concat as (RootDataUIEntity, RootDataUIEntity?) -> C
    }

    @VisibleForTesting data class RootDataValue<C : RootDataUIEntity>(
        private val _concat: AtomicReference<(C, C?) -> C> =
            AtomicReference { old: C, new: C? -> new ?: old },
        @get:VisibleForTesting val liveData: MutableLiveData<C> = MutableLiveData(),
        private val _oldData: AtomicReference<C?> = AtomicReference(liveData.value),
    ) {

        var oldData: C?
            get() = _oldData.get()
            private set(value) = _oldData.set(value)

        var concat: (C, C?) -> C
            get() = _concat.get()
            set(value) = _concat.set(value)

        fun observe(owner: LifecycleOwner, observer: Observer<C>) {
            liveData.distinctUntilChanged().observe(owner, observer)
        }

        fun postValue(newData: C) {
            oldData = newData
            liveData.postValue(newData)
        }
    }
}
