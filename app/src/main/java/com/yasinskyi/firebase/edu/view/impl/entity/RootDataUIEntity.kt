package com.yasinskyi.firebase.edu.view.impl.entity

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.MenuRes
import com.yasinskyi.firebase.edu.util.extension.resources.navigationIcon

sealed class RootDataUIEntity {

    data class Title(val title: CharSequence) : RootDataUIEntity()

    data class Menu(
        @MenuRes val menuId: Int,
        val clearBeforeInflate: Boolean = true,
    ) : RootDataUIEntity()

    data class NavIcon(val isVisible: Boolean) : RootDataUIEntity() {

        fun getNavigationIcon(context: Context): Drawable? =
            if (isVisible) context.navigationIcon else null
    }
}
