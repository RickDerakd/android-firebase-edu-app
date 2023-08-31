package com.yasinskyi.firebase.edu.util.extension.widget

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.yasinskyi.firebase.edu.presentation.base.viewmodel.BaseViewModel
import com.yasinskyi.firebase.edu.util.extension.common.getGenericClass
import com.yasinskyi.firebase.edu.view.base.activity.BaseActivity
import com.yasinskyi.firebase.edu.view.base.fragment.BaseFragment

@get:CheckResult inline val Fragment.baseActivity get() = activity as BaseActivity<*, *>?

@get:CheckResult val Fragment.parentBaseFragment: BaseFragment<*, *>?
    get() = parentFragment as? BaseFragment<*, *>?

@CheckResult fun Fragment.findNearestParentBaseFragment(): BaseFragment<*, *>? =
    parentBaseFragment ?: parentFragment?.findNearestParentBaseFragment()

@get:CheckResult val Fragment.rootBaseFragment: BaseFragment<*, *>? get() = findRootBaseFragment()

@CheckResult private tailrec fun Fragment?.findRootBaseFragment(): BaseFragment<*, *>? =
    if (this?.findNearestParentBaseFragment() == null) {
        this as BaseFragment<*, *>?
    } else {
        parentFragment.findRootBaseFragment()
    }

@get:CheckResult inline val Fragment.viewLifecycleScope: LifecycleCoroutineScope
    get() = viewLifecycleOwner.lifecycleScope

@CheckResult fun Fragment.requireBaseActivity(): BaseActivity<*, *> =
    baseActivity
        ?: throw IllegalStateException("Fragment $this does not have parent base activity")

@CheckResult fun Fragment.requireParentBaseFragment(): BaseFragment<*, *> =
    parentBaseFragment
        ?: throw IllegalStateException("Fragment $this does not have parent base fragment")

@CheckResult fun Fragment.requireNearestParentBaseFragment(): BaseFragment<*, *> =
    findNearestParentBaseFragment()
        ?: throw IllegalStateException("Fragment $this does not have parent base fragment")

@CheckResult fun Fragment.requireRootBaseFragment(): BaseFragment<*, *> =
    rootBaseFragment
        ?: throw IllegalStateException("Fragment $this does not have root base fragment")


@MainThread @CheckResult
fun <VM : BaseViewModel> Fragment.getActivityViewModel(factory: ViewModelProvider.Factory? = null): VM =
    requireBaseActivity().let {
        ViewModelProvider(
            owner = it,
            factory = factory ?: it.defaultViewModelProviderFactory
        )[it.javaClass.getGenericClass(1)]
    }
