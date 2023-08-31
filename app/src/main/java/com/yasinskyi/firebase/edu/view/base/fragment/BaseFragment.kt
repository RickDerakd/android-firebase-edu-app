package com.yasinskyi.firebase.edu.view.base.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.CheckResult
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.appbar.AppBarLayout
import com.yasinskyi.firebase.edu.util.view.navigation.OnBackPressedCallbackWrapper
import com.yasinskyi.firebase.edu.R
import com.yasinskyi.firebase.edu.data.exception.InternetConnectionException
import com.yasinskyi.firebase.edu.presentation.base.viewmodel.BaseViewModel
import com.yasinskyi.firebase.edu.util.extension.widget.createViewModel
import com.yasinskyi.firebase.edu.util.extension.lifecycle.launchOnLifecycleDestroy
import com.yasinskyi.firebase.edu.util.extension.common.unsafeLazy
import com.yasinskyi.firebase.edu.util.extension.lifecycle.launchOnLifecycle
import com.yasinskyi.firebase.edu.util.extension.lifecycle.launchOnLifecycleResume
import com.yasinskyi.firebase.edu.util.extension.lifecycle.observeWhenCreated
import com.yasinskyi.firebase.edu.util.extension.widget.baseActivity
import com.yasinskyi.firebase.edu.util.extension.widget.findNearestParentBaseFragment
import com.yasinskyi.firebase.edu.util.extension.widget.inflateBinding
import com.yasinskyi.firebase.edu.util.extension.widget.inflateMenu
import com.yasinskyi.firebase.edu.util.extension.widget.rootBaseFragment
import com.yasinskyi.firebase.edu.util.view.lifecycle.lifecycle
import com.yasinskyi.firebase.edu.view.BaseView
import com.yasinskyi.firebase.edu.view.impl.entity.RootDataUIEntity.Menu
import com.yasinskyi.firebase.edu.view.impl.entity.RootDataUIEntity.Title
import com.yasinskyi.firebase.edu.view.impl.entity.RootDataUIEntity.NavIcon
import timber.log.Timber
import kotlin.properties.Delegates

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> :
    InjectableFragment(),
    BaseView<VB, VM> {

    protected val viewModel: VM by unsafeLazy(::createViewModel)

    protected var binding: VB? = null
        private set

    val navController: NavController by unsafeLazy {
        findNavController()
    }

    protected val toolbar: Toolbar? by Delegates.lifecycle(::getViewLifecycleOwner) {
        binding?.root?.findViewById(R.id.toolbar)
    }

    protected val appBarLayout: AppBarLayout? by Delegates.lifecycle(::getViewLifecycleOwner) {
        binding?.root?.findViewById(R.id.appBarLayout)
    }

    protected val isRoot: Boolean by unsafeLazy { appBarLayout != null }

    @get:StyleRes protected open val themeRes: Int = R.style.Theme_MemarLite_Animation

    protected open val isNavigationIconEnabled: Boolean
        get() = parentFragmentManager.backStackEntryCount > 0 ||
                findNearestParentBaseFragment()?.isNavigationIconEnabled == true

    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater =
        super.onGetLayoutInflater(savedInstanceState)
            .cloneInContext(ContextThemeWrapper(requireContext(), themeRes))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflateBinding(inflater, container, false)
        .also(::binding::set)
        .also { viewLifecycleOwner.launchOnLifecycleDestroy { binding = null } }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireBinding().let { binding ->
            initBackPressListener()
            initToolbar()
            onViewBound(binding, savedInstanceState)
            listenViewModel(viewModel)
            listenRootData(binding)
        }
    }

    private fun initBackPressListener() {
        val onBackPressedCallback = OnBackPressedCallbackWrapper(false, ::onBackPressed)

        viewLifecycleOwner.launchOnLifecycle { event ->
            onBackPressedCallback.isEnabled = event == Lifecycle.Event.ON_RESUME
        }

        onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    protected open fun onBackPressed() {
        if (navController.navigateUp()) {
            Timber.i("Back navigation completed")
        } else {
            findNearestParentBaseFragment()?.onBackPressed()
                ?: requireActivity().finishAfterTransition()
        }
    }

    private fun listenRootData(binding: VB) {
        viewModel.observe(viewLifecycleOwner) { rootData ->
            when (rootData) {
                is NavIcon -> onNavIconDataChanged(binding, rootData)
                is Title -> onTitleDataChanged(binding, rootData)
                is Menu -> onMenuDataChanged(binding, rootData)
            }
        }
    }

    protected open fun onMenuDataChanged(binding: VB, menu: Menu) {
        toolbar?.inflateMenu(menu.menuId, menu.clearBeforeInflate)
    }

    @CallSuper override fun listenViewModel(viewModel: VM) {
        observeWhenCreated(viewModel.loadingState, ::onLoading)
        observeWhenCreated(viewModel.unexpectedErrorState, ::handleError)
    }

    @CheckResult protected fun requireBinding(): VB =
        binding ?: throw IllegalStateException("Binding in fragment $this is null")

    protected open fun onTitleDataChanged(binding: VB, title: Title) {
        toolbar?.title = title.title
    }

    protected open fun onNavIconDataChanged(binding: VB, navIcon: NavIcon) {
        toolbar?.navigationIcon = navIcon.getNavigationIcon(binding.root.context)
    }

    protected open fun onNavigationIconClick() {
        onBackPressedDispatcher.onBackPressed()
    }

    private fun initToolbar() {
        toolbar?.let { toolbar ->
            toolbar.setOnMenuItemClickListener(::onMenuItemClick)
            toolbar.setNavigationOnClickListener { onNavigationIconClick() }
        }

        viewLifecycleOwner.launchOnLifecycleResume { viewModel.invalidateRootData() }
    }

    private fun onMenuItemClick(menuItem: MenuItem): Boolean {
        if (isHidden || !isResumed) return false
        if (onToolbarItemSelected(menuItem)) return true

        return childFragmentManager
            .fragments
            .asSequence()
            .map { childFragment ->
                if (childFragment is BaseFragment<*, *>) {
                    listOf(childFragment)
                } else {
                    childFragment.childFragmentManager.fragments
                }
            }.flatten()
            .mapNotNull { it as? BaseFragment<*, *>? }
            .any { it.onMenuItemClick(menuItem) }
    }

    @Deprecated(
        message = "Deprecated in Java",
        replaceWith = ReplaceWith("onToolbarItemSelected(menuItem)"),
        level = DeprecationLevel.ERROR,
    )
    @SuppressLint("DeprecatedDetector")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        @Suppress("DEPRECATION")
        return super.onOptionsItemSelected(item)
    }

    protected open fun onToolbarItemSelected(menuItem: MenuItem) = false

    fun invalidateOptionsMenu() {
        if (isRoot) toolbar?.invalidateMenu() else rootBaseFragment?.toolbar?.invalidateMenu()
    }

    protected fun showMessage(
        @StringRes messageTitleId: Int,
        @StringRes messageResId: Int,
        @StringRes positiveButtonTextId: Int
    ) {
        baseActivity
            ?.provideMessageInterface()
            ?.showMessage(
                messageTitleId,
                messageResId,
                positiveButtonTextId,
            )
    }

    protected fun showMessageWithPositiveAction(
        @StringRes messageTitleId: Int,
        @StringRes messageResId: Int,
        @StringRes positiveButtonTextId: Int,
        @StringRes negativeButtonTextId: Int,
        onPositiveButtonCallback: () -> Unit,
    ) {
        baseActivity
            ?.provideMessageInterface()
            ?.showMessageWithPositiveAction(
                messageTitleId,
                messageResId,
                positiveButtonTextId,
                negativeButtonTextId,
                onPositiveButtonCallback,
            )
    }

    override fun onLoading(isLoading: Boolean) {
        baseActivity?.onLoading(isLoading)
    }

    override fun handleError(throwable: Throwable) {
        when (throwable) {
            is InternetConnectionException -> {
                showMessage(
                    R.string.error_message_title,
                    R.string.no_internet_connection_message,
                    R.string.ok,
                )
            }

            else -> {
                baseActivity?.handleError(throwable)
            }
        }
    }
}