package com.yasinskyi.firebase.edu.view.impl.dialog.alert

import android.content.Context
import androidx.annotation.StringRes
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yasinskyi.firebase.edu.view.base.dialog.MessageInterface

class AlertMessageInterface(
    private val context: Context,
) : MessageInterface {

    override fun showMessage(
        @StringRes messageTitleId: Int,
        @StringRes messageResId: Int,
        @StringRes positiveButtonTextId: Int,
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle(context.getString(messageTitleId))
            .setMessage(context.getString(messageResId))
            .setPositiveButton(context.getString(positiveButtonTextId)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun showMessageWithPositiveAction(
        @StringRes messageTitleId: Int,
        @StringRes messageResId: Int,
        @StringRes positiveButtonTextId: Int,
        @StringRes negativeButtonTextId: Int,
        onPositiveButtonCallback: () -> Unit,
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle(context.getString(messageTitleId))
            .setMessage(context.getString(messageResId))
            .setPositiveButton(context.getString(positiveButtonTextId)) { _, _ ->
                onPositiveButtonCallback()
            }
            .setNegativeButton(context.getString(negativeButtonTextId)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}