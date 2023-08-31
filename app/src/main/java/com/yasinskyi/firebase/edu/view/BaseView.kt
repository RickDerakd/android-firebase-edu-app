package com.yasinskyi.firebase.edu.view

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.yasinskyi.firebase.edu.presentation.base.viewmodel.BaseViewModel

interface BaseView<VB : ViewBinding, VM : BaseViewModel> {

    fun onViewBound(binding: VB, savedInstanceState: Bundle?) {
        // Override if need and make other initialization
    }

    fun listenViewModel(viewModel: VM) {
        // Override if need and make other initialization
    }

    fun onLoading(isLoading: Boolean) {
        TODO("Override and implement on loading logic")
    }

    fun handleError(throwable: Throwable) {
        TODO("Override and implement error handling logic")
    }
}
