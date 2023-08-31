package com.yasinskyi.firebase.edu.util.formatter

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {

    private const val simpleDatePattern: String = "_yyyy-MM-dd_HH-mm-ss"

    @SuppressLint("SimpleDateFormat", "ConstantLocale")
    private val simpleDateFormatter = SimpleDateFormat(simpleDatePattern, Locale.getDefault())

    fun formatDate(date: Date): String {
        return simpleDateFormatter.format(date)
    }
}