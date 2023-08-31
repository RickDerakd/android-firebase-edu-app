package com.yasinskyi.firebase.edu.util.extension.resources

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.CheckResult
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import com.yasinskyi.firebase.edu.R

@CheckResult fun Context.getDrawableCompat(@DrawableRes resId: Int): Drawable? =
    AppCompatResources.getDrawable(this, resId)

@get:CheckResult inline val Context.navigationIcon: Drawable?
    get() =
        getDrawableCompat(
            resId = TypedValue().also {
                theme?.resolveAttribute(R.attr.navigationIcon, it, true)
            }.resourceId
        )