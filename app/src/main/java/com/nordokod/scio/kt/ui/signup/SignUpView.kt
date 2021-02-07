package com.nordokod.scio.kt.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.nordokod.scio.R
import kotlinx.android.synthetic.main.fragment_signup_view.*
import org.koin.android.viewmodel.ext.android.viewModel
import androidx.lifecycle.Observer
import com.nordokod.scio.kt.utils.getEnumErrorMessage

class SignUpView: Fragment() {
    private val viewModel by viewModel<SignUpViewModel>()
    private val navController : NavController by lazy { findNavController()}
    override fun onStart() {
        super.onStart()
        initListeners()
        observeLiveData()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup_view, container, false)
    }

    fun initListeners(){
        SU_BTN_Signup.setOnClickListener{
            navController.navigate(R.id.action_global_loadingView)
            viewModel.signUp(SU_ET_Mail.text.toString(),SU_ET_Password.text.toString(),SU_ET_ConfirmPassword.text.toString())
        }
    }

    private fun observeLiveData(){
        val context = this.context
        if(context!=null){
            viewModel.error.observe(
                    viewLifecycleOwner,
                    Observer {
                        it.getContentIfNotHandled()
                                ?.getEnumErrorMessage()
                                ?.showMessage(context)
                        dismissDialog()
                    }
            )
            viewModel.successMessage.observe(
                    viewLifecycleOwner,
                    Observer {
                        it.getContentIfNotHandled()
                                ?.showMessage(context)
                        dismissDialog()
                    }
            )
            viewModel.signUpAction.observe(
                viewLifecycleOwner,
                    Observer {
                        navController.navigate(R.id.action_signupView_to_verifyMailView)
                    }
            )
        }
    }
    private fun dismissDialog(){
        val dialog = parentFragmentManager.findFragmentById(R.id.loadingView)
        if(dialog is DialogFragment){
            dialog.dialog?.dismiss()
        }
    }

}