package com.nordokod.scio.kt.ui.signup

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.nordokod.scio.R
import kotlinx.android.synthetic.main.fragment_signup_view.*
import org.koin.android.viewmodel.ext.android.viewModel
import androidx.lifecycle.Observer
import com.nordokod.scio.kt.constants.Generic
import com.nordokod.scio.kt.constants.enums.SendoScreen
import com.nordokod.scio.kt.utils.AnalyticsHelper
import com.nordokod.scio.kt.utils.getEnumErrorMessage

class SignUpView: Fragment() {
    private val viewModel by this.viewModel<SignUpViewModel>()
    private val navController : NavController by lazy { findNavController()}
    private var isDisplaying = false
    private val countDownTimer = object : CountDownTimer(Generic.BEFORE_LOADING_TIME, Generic.BEFORE_LOADING_TIME) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {
            isDisplaying = true
            navController.navigate(R.id.action_global_loadingView)
        }
    }

    override fun onStart() {
        super.onStart()
        AnalyticsHelper.recordScreenView(SendoScreen.SIGN_UP_SCREEN,this::class.simpleName.toString())
        initListeners()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        observeLiveData()
        return inflater.inflate(R.layout.fragment_signup_view, container, false)
    }

    fun initListeners(){
        SU_BTN_Signup.setOnClickListener{
            countDownTimer.start()
            viewModel.signUp(SU_ET_Mail.text.toString(),SU_ET_Password.text.toString(),SU_ET_ConfirmPassword.text.toString())
        }
        SU_BTN_Cancel.setOnClickListener {
            navController.navigate(R.id.action_signupView_to_loginView)
        }
    }

    private fun observeLiveData(){
        val context = this.context
        if(context!=null){
            viewModel.error.observe(
                    viewLifecycleOwner,
                    Observer {
                        dismissDialog()
                        it.getEnumErrorMessage().showMessage(context)
                    }
            )
            viewModel.successMessage.observe(
                    viewLifecycleOwner,
                    Observer {
                        dismissDialog()
                        it.showMessage(context)
                    }
            )
            viewModel.signUpAction.observe(
                    viewLifecycleOwner,
                    Observer {
                        dismissDialog()
                        navController.navigate(R.id.action_signupView_to_verifyMailView)
                    }
            )
        }
    }
    private fun dismissDialog(){
        if(isDisplaying){
            navController.popBackStack()
            countDownTimer.cancel()
            isDisplaying = false
        } else countDownTimer.cancel()
    }

}