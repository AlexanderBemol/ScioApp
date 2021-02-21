package com.nordokod.scio.kt.modules

import com.nordokod.scio.kt.ui.login.LoginViewModel
import com.nordokod.scio.kt.ui.main.MainViewModel
import com.nordokod.scio.kt.ui.permissions.PermissionsViewModel
import com.nordokod.scio.kt.ui.signup.SignUpViewModel
import com.nordokod.scio.kt.ui.splash.SplashViewModel
import com.nordokod.scio.kt.ui.verifymail.VerifyMailViewModel
import com.nordokod.scio.kt.utils.PermissionsCheck
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module{
    viewModel { LoginViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { VerifyMailViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { PermissionsViewModel(PermissionsCheck(),get()) }
}