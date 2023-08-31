package com.yasinskyi.firebase.edu.presentation.impl.viewmodel.main

import com.yasinskyi.firebase.edu.presentation.base.viewmodel.BaseViewModel
import com.yasinskyi.firebase.edu.presentation.impl.entity.PresentationDataEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    presenterData: PresentationDataEntity,
) : BaseViewModel(presenterData)
