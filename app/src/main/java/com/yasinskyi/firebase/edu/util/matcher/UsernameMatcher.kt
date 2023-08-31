package com.yasinskyi.firebase.edu.util.matcher

import java.util.regex.Pattern

object UsernameMatcher {

    private const val USERNAME_PATTERN = "^[A-z0-9\\._-]{5,20}$"
    private val pattern: Pattern = Pattern.compile(USERNAME_PATTERN)

    fun isUsername(username: String?): Boolean {
        return if (username != null) {
            pattern.matcher(username as CharSequence).matches()
        } else {
            false
        }
    }
}