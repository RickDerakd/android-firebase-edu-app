package com.yasinskyi.firebase.edu.view.impl.adapter.home

import android.view.ViewGroup
import androidx.navigation.NavController
import com.yasinskyi.firebase.edu.databinding.UserItemBinding
import com.yasinskyi.firebase.edu.presentation.impl.entity.User
import com.yasinskyi.firebase.edu.view.base.adapter.BaseAdapter
import com.yasinskyi.firebase.edu.view.base.adapter.BaseViewHolder

class UserAdapter(
    private val navController: NavController,
) : BaseAdapter<User>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<User, UserItemBinding> {
        return UserViewHolder(parent, navController)
    }
}