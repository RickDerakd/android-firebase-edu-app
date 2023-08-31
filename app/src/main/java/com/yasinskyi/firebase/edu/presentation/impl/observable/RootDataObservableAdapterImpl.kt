package com.yasinskyi.firebase.edu.presentation.impl.observable

import android.content.res.Resources
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import com.hadilq.liveevent.LiveEvent
import com.yasinskyi.firebase.edu.presentation.base.observable.RootDataObservableAdapter
import com.yasinskyi.firebase.edu.presentation.base.observable.WriteRootDataObservable
import com.yasinskyi.firebase.edu.view.impl.entity.RootDataUIEntity
import com.yasinskyi.firebase.edu.view.impl.entity.RootDataUIEntity.Menu
import com.yasinskyi.firebase.edu.view.impl.entity.RootDataUIEntity.NavIcon
import com.yasinskyi.firebase.edu.view.impl.entity.RootDataUIEntity.Title
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

class RootDataObservableAdapterImpl @Inject constructor(
    private val rootDataObservable: WriteRootDataObservable,
    private val resourcesProvider: Provider<Resources>,
) : RootDataObservableAdapter,
    WriteRootDataObservable by rootDataObservable {

    private val resources: Resources get() = resourcesProvider.get()

    private var dataForInvalidateCurrentScreenMap =
        RootDataUIEntity::class
            .sealedSubclasses
            .associateWith<KClass<out RootDataUIEntity>, RootDataUIEntity?> { null }
            .toMutableMap()

    override val navigationIconEnableLiveEvent = LiveEvent<Unit>()

    override fun getTitle(): Title? = get(Title::class)
    override fun postTitle(title: Title): Boolean = post(title)
    override fun postTitle(title: String) = postTitle(Title(title))
    override fun postTitle(@StringRes id: Int) = postTitle(resources.getString(id))
    override fun postTitle(
        @StringRes id: Int,
        vararg args: Any,
    ) = postTitle(resources.getString(id, *args))

    override fun getMenu(): Menu? = get(Menu::class)
    override fun postMenu(menu: Menu): Boolean = post(menu)
    override fun postMenu(@MenuRes id: Int) = postMenu(Menu(id))

    override fun getNavIcon(): NavIcon? = get(NavIcon::class)
    override fun postNavIcon(navIcon: NavIcon) = post(navIcon)
    override fun postNavIcon(enabled: Boolean) = postNavIcon(NavIcon(enabled))

    override fun post(newData: RootDataUIEntity): Boolean {
        dataForInvalidateCurrentScreenMap[newData::class] = newData
        return rootDataObservable.post(newData)
    }

    override fun invalidateRootData(key: KClass<out RootDataUIEntity>?) {
        when (key) {
            NavIcon::class -> navigationIconEnableLiveEvent.postValue(Unit)
            null -> dataForInvalidateCurrentScreenMap.keys.forEach(::invalidateRootData)

            else -> {
                key.let(dataForInvalidateCurrentScreenMap::get)?.let(rootDataObservable::post)
                    ?: Timber.i("Cached value is null by key=$key")
            }
        }
    }
}