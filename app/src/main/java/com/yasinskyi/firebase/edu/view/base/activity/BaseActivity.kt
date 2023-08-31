package com.yasinskyi.firebase.edu.view.base.activity

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding
import com.yasinskyi.firebase.edu.R
import com.yasinskyi.firebase.edu.util.extension.widget.createViewModel
import com.yasinskyi.firebase.edu.presentation.base.viewmodel.BaseViewModel
import com.yasinskyi.firebase.edu.util.extension.common.unsafeLazy
import com.yasinskyi.firebase.edu.util.extension.lifecycle.observeWhenCreated
import com.yasinskyi.firebase.edu.util.extension.widget.inflateBinding
import com.yasinskyi.firebase.edu.view.BaseView
import com.yasinskyi.firebase.edu.view.base.dialog.MessageInterface

abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel> :
    InjectableAppCompatActivity(),
    BaseView<VB, VM> {

    protected val viewModel: VM by unsafeLazy(::createViewModel)

    protected val binding: VB by unsafeLazy { inflateBinding(layoutInflater) }

    val navController: NavController by unsafeLazy {
        findNavController(R.id.fragmentLoginContainerView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        listenViewModel(viewModel)
        onViewBound(binding, savedInstanceState)
    }

    @CallSuper override fun listenViewModel(viewModel: VM) {
        observeWhenCreated(viewModel.loadingState, ::onLoading)
        observeWhenCreated(viewModel.unexpectedErrorState, ::handleError)
    }

    override fun handleError(throwable: Throwable) {
        alertMessageInterface
            .showMessage(R.string.error_message_title, R.string.unknown_error_message, R.string.ok)
    }

    fun provideMessageInterface(): MessageInterface {
        return alertMessageInterface
    }
}