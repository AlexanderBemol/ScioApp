package com.nordokod.scio.kt.modules

import com.nordokod.scio.kt.ui.login.LoginViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module{
    viewModel { LoginViewModel(get())}
}