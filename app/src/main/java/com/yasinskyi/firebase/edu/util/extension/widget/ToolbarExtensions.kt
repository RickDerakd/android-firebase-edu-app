package com.yasinskyi.firebase.edu.util.extension.widget

import androidx.annotation.MenuRes
import androidx.appcompat.widget.Toolbar

fun Toolbar.inflateMenu(@MenuRes menuRes: Int, clearBeforeInflate: Boolean = true) {
    if (clearBeforeInflate) {
        menu?.clear()
    }
    inflateMenu(menuRes)
}
