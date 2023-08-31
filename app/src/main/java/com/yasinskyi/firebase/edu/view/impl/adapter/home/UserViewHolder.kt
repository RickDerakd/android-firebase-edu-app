package com.yasinskyi.firebase.edu.view.impl.adapter.home

import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.navigation.NavController
import com.yasinskyi.firebase.edu.R
import com.yasinskyi.firebase.edu.databinding.UserItemBinding
import com.yasinskyi.firebase.edu.presentation.impl.entity.User
import com.yasinskyi.firebase.edu.view.base.adapter.BaseViewHolder

class UserViewHolder(
    parent: ViewGroup,
    private val navController: NavController
) : BaseViewHolder<User, UserItemBinding>(parent, UserItemBinding::inflate) {

    @CallSuper override fun bind(item: User) {
        super.bind(item)

        with(binding) {
            userNickname.text = item.username
            root.setOnClickListener {
                navController.navigate(R.id.actionHomeFragmentToPhotoFragment)
            }
        }
    }
}