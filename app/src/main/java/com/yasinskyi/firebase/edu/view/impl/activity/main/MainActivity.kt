package com.yasinskyi.firebase.edu.view.impl.activity.main

import androidx.core.view.isVisible
import com.yasinskyi.firebase.edu.databinding.ActivityMainBinding
import com.yasinskyi.firebase.edu.presentation.impl.viewmodel.main.MainViewModel
import com.yasinskyi.firebase.edu.view.base.activity.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override fun onLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }
}