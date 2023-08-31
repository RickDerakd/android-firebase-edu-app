package com.yasinskyi.firebase.edu.util.extension.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CheckResult
import com.yasinskyi.firebase.edu.util.extension.content.layoutInflater

@get:CheckResult inline val View.marginLayoutParams
    get() = layoutParams as ViewGroup.MarginLayoutParams

@get:CheckResult inline val View.layoutInflater: LayoutInflater
    get() = context.layoutInflater
