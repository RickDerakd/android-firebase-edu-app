package com.yasinskyi.firebase.edu.view.base.dialog

import androidx.annotation.StringRes

interface MessageInterface {

    fun showMessage(
        @StringRes messageTitleId: Int,
        @StringRes messageResId: Int,
        @StringRes positiveButtonTextId: Int
    )

    fun showMessageWithPositiveAction(
        @StringRes messageTitleId: Int,
        @StringRes messageResId: Int,
        @StringRes positiveButtonTextId: Int,
        @StringRes negativeButtonTextId: Int,
        onPositiveButtonCallback: () -> Unit,
    )
}