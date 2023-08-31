package com.yasinskyi.firebase.edu.view.impl.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil

class DefaultDiffUtilItemCallback<T : Any>(
    val overrideAreItemsTheSame: (T, T) -> Boolean,
    val overrideAreContentsTheSame: (T, T) -> Boolean,
) : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        overrideAreItemsTheSame(oldItem, newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        overrideAreContentsTheSame(oldItem, newItem)
}
