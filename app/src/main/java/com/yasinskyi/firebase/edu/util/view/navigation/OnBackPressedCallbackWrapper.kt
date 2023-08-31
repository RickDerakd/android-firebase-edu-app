package com.yasinskyi.firebase.edu.util.view.navigation

import androidx.activity.OnBackPressedCallback

class OnBackPressedCallbackWrapper(
    enabled: Boolean,
    private val handleOnBackPressed: () -> Unit,
) : OnBackPressedCallback(enabled) {

    override fun handleOnBackPressed() {
        handleOnBackPressed.invoke()
    }
}
