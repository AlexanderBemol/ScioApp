package com.nordokod.scio.kt.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.nordokod.scio.R
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.viewmodel.ext.android.viewModel
import com.nordokod.scio.kt.utils.getEnumErrorMessage

class LoginView: AppCompatActivity() {
    private val viewModel by viewModel<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initListeners()
        observeLiveData()
    }

    fun initListeners(){
        BTN_Login.setOnClickListener {
            viewModel.signInWithMail(ET_Mail.text.toString(),ET_Password.text.toString())
        }
    }

    private fun observeLiveData(){
        viewModel.error.observe(
                this, //in fragments use viewLifecycleOwner
                Observer { it.getEnumErrorMessage().showMessage(this)}
        )
        viewModel.successMessage.observe(
                this,
                Observer { it.showMessage(this) }
        )

    }
}