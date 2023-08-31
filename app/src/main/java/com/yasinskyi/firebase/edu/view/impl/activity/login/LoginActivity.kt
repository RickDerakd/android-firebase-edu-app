package com.yasinskyi.firebase.edu.view.impl.activity.login

import androidx.core.view.isVisible
import com.yasinskyi.firebase.edu.databinding.ActivityLoginBinding
import com.yasinskyi.firebase.edu.presentation.impl.viewmodel.login.LoginViewModel
import com.yasinskyi.firebase.edu.view.base.activity.BaseActivity

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    override fun onLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }
}