package com.yasinskyi.firebase.edu.presentation.base.observable

import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import com.hadilq.liveevent.LiveEvent
import com.yasinskyi.firebase.edu.R
import com.yasinskyi.firebase.edu.view.impl.entity.RootDataUIEntity
import com.yasinskyi.firebase.edu.view.impl.entity.RootDataUIEntity.Menu
import com.yasinskyi.firebase.edu.view.impl.entity.RootDataUIEntity.NavIcon
import com.yasinskyi.firebase.edu.view.impl.entity.RootDataUIEntity.Title
import kotlin.reflect.KClass

interface RootDataObservableAdapter : RootDataObservable {

    val navigationIconEnableLiveEvent: LiveEvent<Unit>

    fun getTitle(): Title?
    fun postTitle(title: Title): Boolean
    fun postTitle(title: String): Boolean
    fun postTitle(@StringRes id: Int): Boolean
    fun postTitle(@StringRes id: Int, vararg args: Any): Boolean

    fun getMenu(): Menu?
    fun postMenu(menu: Menu): Boolean
    fun postMenu(@MenuRes id: Int = R.menu.menu_empty): Boolean

    fun getNavIcon(): NavIcon?
    fun postNavIcon(navIcon: NavIcon): Boolean
    fun postNavIcon(enabled: Boolean): Boolean

    fun invalidateRootData(key: KClass<out RootDataUIEntity>? = null)
}
