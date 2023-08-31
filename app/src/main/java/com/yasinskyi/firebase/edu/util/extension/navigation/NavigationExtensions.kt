package com.yasinskyi.firebase.edu.util.extension.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.CheckResult
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import com.yasinskyi.firebase.edu.util.extension.common.tryOrNull
import com.yasinskyi.firebase.edu.util.extension.widget.baseActivity
import com.yasinskyi.firebase.edu.util.extension.widget.findNearestParentBaseFragment
import com.yasinskyi.firebase.edu.util.extension.widget.requireBaseActivity
import com.yasinskyi.firebase.edu.util.extension.widget.requireNearestParentBaseFragment
import timber.log.Timber
import kotlin.reflect.KClass

val Fragment.activityNavController: NavController?
    get() = baseActivity?.navController

fun Fragment.requireActivityNavController(): NavController =
    requireBaseActivity().navController

val Fragment.parentNavController: NavController?
    get() = findNearestParentBaseFragment()?.navController

fun Fragment.requireParentNavController(): NavController =
    requireNearestParentBaseFragment().navController

fun Fragment.navigate(
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
    navController: NavController = findNavController(resId),
) {
    navController.navigate(resId, args, navOptions, navigatorExtras)
}

fun Fragment.navigateSafe(
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
    navController: NavController? = tryOrNull { findNavController(resId) },
): Boolean =
    if (navController == null) {
        Timber.w("Nav controller wasn't found for resource $resId")
        false
    } else {
        navController.navigateSafe(resId, args, navOptions, navigatorExtras)
    }

fun Fragment.findNavController(
    @IdRes resId: Int,
): NavController {
    val navController = findNavController()

    val currentNode =
        if (navController.backQueue.isEmpty()) {
            navController.graph
        } else {
            navController.backQueue.last().destination
        }

    val navigableObject = currentNode.getAction(resId) ?: navController.graph.findNode(resId)

    return if (navigableObject == null) {
        parentFragment?.findNavController(resId)
            ?: throw IllegalStateException("no current navigation node")
    } else {
        navController
    }
}

fun NavController?.navigateSafe(
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) = runCatching {
    this?.navigate(
        resId = resId,
        args = args,
        navOptions = navOptions,
        navigatorExtras = navigatorExtras,
    )
}.onFailure(Timber::e).isSuccess

fun Context.navigate(
    kClass: KClass<out Activity>,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
): NavDestination? =
    navigate(
        intent = Intent(this, kClass.java),
        args = args,
        navOptions = navOptions,
        navigatorExtras = navigatorExtras,
    )

fun Context.navigate(
    intent: Intent,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
): NavDestination? =
    ActivityNavigator(this).navigate(
        intent = intent,
        args = args,
        navOptions = navOptions,
        navigatorExtras = navigatorExtras,
    )

fun ActivityNavigator.navigate(
    intent: Intent,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
): NavDestination? =
    intent.getDestination(this).navigate(
        navigator = this,
        args = args,
        navOptions = navOptions,
        navigatorExtras = navigatorExtras,
    )

@CheckResult
fun Intent.getDestination(navigator: ActivityNavigator): ActivityNavigator.Destination =
    navigator.createDestination().setIntent(this)

fun ActivityNavigator.Destination.navigate(
    navigator: ActivityNavigator,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
): NavDestination? =
    navigator.navigate(
        destination = this,
        args = args,
        navOptions = navOptions,
        navigatorExtras = navigatorExtras,
    )
