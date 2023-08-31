package com.yasinskyi.firebase.edu.view.base.fragment

import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint abstract class InjectableFragment : Fragment() {

    @Inject protected lateinit var onBackPressedDispatcher: OnBackPressedDispatcher
}
