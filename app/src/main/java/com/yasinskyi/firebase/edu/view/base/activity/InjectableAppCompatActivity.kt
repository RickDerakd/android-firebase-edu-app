package com.yasinskyi.firebase.edu.view.base.activity

import androidx.appcompat.app.AppCompatActivity
import com.yasinskyi.firebase.edu.view.base.dialog.MessageInterface
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint abstract class InjectableAppCompatActivity : AppCompatActivity() {

    @Inject protected lateinit var alertMessageInterface: MessageInterface
}
