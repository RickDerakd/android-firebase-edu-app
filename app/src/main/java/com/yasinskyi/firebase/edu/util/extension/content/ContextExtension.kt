package com.yasinskyi.firebase.edu.util.extension.content

import android.content.Context
import android.net.ConnectivityManager
import android.view.LayoutInflater
import androidx.annotation.CheckResult

inline val Context.connectivityManager: ConnectivityManager?
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

@get:CheckResult inline val Context.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this)