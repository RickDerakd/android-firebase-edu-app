package com.yasinskyi.firebase.edu.view.base.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.yasinskyi.firebase.edu.view.impl.adapter.diffutil.DefaultDiffUtilItemCallback
import java.util.Objects

abstract class BaseListAdapter<T : Any>(
    areItemsTheSame: (T, T) -> Boolean = Objects::equals,
    areContentsTheSame: (T, T) -> Boolean = Objects::equals,
    callback: DiffUtil.ItemCallback<T> = DefaultDiffUtilItemCallback(
        overrideAreItemsTheSame = areItemsTheSame,
        overrideAreContentsTheSame = areContentsTheSame,
    ),
    stateRestorationPolicy: StateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY,
) : ListAdapter<T, BaseViewHolder<T, ViewBinding>>(callback) {

    init {
        this.stateRestorationPolicy = stateRestorationPolicy
    }

    public override fun getItem(position: Int): T = super.getItem(position)

    override fun onBindViewHolder(holder: BaseViewHolder<T, ViewBinding>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewRecycled(holder: BaseViewHolder<T, ViewBinding>) {
        holder.unbind()
        super.onViewRecycled(holder)
    }
}
