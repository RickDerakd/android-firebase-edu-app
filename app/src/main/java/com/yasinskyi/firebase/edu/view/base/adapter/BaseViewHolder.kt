package com.yasinskyi.firebase.edu.view.base.adapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.yasinskyi.firebase.edu.util.extension.widget.layoutInflater

abstract class BaseViewHolder<T : Any, out VB : ViewBinding>(
    protected val binding: VB,
) : RecyclerView.ViewHolder(binding.root) {

    constructor(
        parent: ViewGroup,
        inflate: (LayoutInflater, ViewGroup, Boolean) -> VB,
    ) : this(inflate(parent.layoutInflater, parent, false))

    protected val context: Context get() = itemView.context

    protected val resources: Resources get() = context.resources

    lateinit var item: T
        private set

    @CallSuper open fun bind(item: T) {
        this.item = item
    }

    @CallSuper open fun unbind() {
        // None
    }
}
