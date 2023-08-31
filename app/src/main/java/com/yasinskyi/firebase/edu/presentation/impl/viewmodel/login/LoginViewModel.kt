package com.yasinskyi.firebase.edu.presentation.impl.viewmodel.login

import com.yasinskyi.firebase.edu.presentation.base.viewmodel.BaseViewModel
import com.yasinskyi.firebase.edu.presentation.impl.entity.PresentationDataEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    presenterData: PresentationDataEntity,
) : BaseViewModel(presenterData)
