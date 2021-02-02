package com.nordokod.scio.kt.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.nordokod.scio.R
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.viewmodel.ext.android.viewModel
import com.nordokod.scio.kt.utils.getEnumErrorMessage

class LoginView: Fragment() {
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
        return inflater.inflate(R.layout.fragment_login_view, container, false)
    }

    fun initListeners(){
        BTN_Login.setOnClickListener {
            viewModel.signInWithMail(ET_Mail.text.toString(),ET_Password.text.toString())
        }
    }

    private fun observeLiveData(){
        val context = this.context
        if (context != null){
            viewModel.error.observe(
                    viewLifecycleOwner,
                    Observer { it.getEnumErrorMessage().showMessage(context)}
            )
            viewModel.successMessage.observe(
                    this,
                    Observer { it.showMessage(context) }
            )
        }
    }
}