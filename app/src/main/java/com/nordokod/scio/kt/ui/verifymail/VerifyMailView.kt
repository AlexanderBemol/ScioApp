package com.nordokod.scio.kt.ui.verifymail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nordokod.scio.R
import com.nordokod.scio.kt.ui.login.LoginViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class VerifyMailView : Fragment() {
    private val viewModel by viewModel<LoginViewModel>()
    override fun onStart() {
        super.onStart()
        initListeners()
        observeLiveData()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_verify_mail_view, container, false)
    }

    fun initListeners(){
    }

    private fun observeLiveData(){
        val context = this.context
    }

}