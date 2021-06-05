package com.nordokod.scio.kt.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.nordokod.scio.R
import com.nordokod.scio.kt.constants.Generic
import com.nordokod.scio.kt.constants.enums.ErrorMessage
import com.nordokod.scio.kt.utils.getEnumErrorMessage
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.viewmodel.ext.android.viewModel

class LoginView: Fragment() {
    private val viewModel by viewModel<LoginViewModel>()
    private val navController : NavController by lazy { findNavController()}
    private val callbackManager by lazy { CallbackManager.Factory.create() }
    private val mGoogleSignInClient by lazy {
        this.activity?.let { GoogleSignIn
                .getClient(it
                    ,GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build() )
        }
    }
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
        initListeners()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        observeLiveData()
        return inflater.inflate(R.layout.fragment_login_view, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 9001) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                countDownTimer.start()
                viewModel.loginWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                context?.let { ErrorMessage.GOOGLE_EXCEPTION.showMessage(it) }
            }
        }

    }

    fun initListeners(){
        BTN_Login.setOnClickListener {
            countDownTimer.start()
            viewModel.signInWithMail(ET_Mail.text.toString(),ET_Password.text.toString())
        }
        BTN_Signup.setOnClickListener{
            navController.navigate(R.id.action_loginView_to_signupView)
        }
        BTN_Facebook.setOnClickListener {
            countDownTimer.start()
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email", "public_profile"))
            LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult>{
                override fun onSuccess(loginResult: LoginResult) {
                    viewModel.loginWithFacebook(loginResult.accessToken.token)
                }

                override fun onCancel() {

                }

                override fun onError(error: FacebookException) {
                    context?.let { ctx -> ErrorMessage.FACEBOOK_EXCEPTION.showMessage(ctx) }
                }

            })
        }
        BTN_Google.setOnClickListener {
            val signInIntent = mGoogleSignInClient?.signInIntent
            startActivityForResult(signInIntent, 9001)
        }
    }

    private fun observeLiveData(){
        val context = this.context
        if (context != null){
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
            viewModel.loginAction.observe(
                    viewLifecycleOwner,
                    Observer {
                        if(it == LoginActions.GO_TO_MAIN){
                            navController.navigate(R.id.action_loginView_to_mainView)
                        } else if (it == LoginActions.GO_TO_VERIFY_MAIL){
                            navController.navigate(R.id.action_loginView_to_verifyMailView)
                        }
                    }
            )
        }
    }
    private fun dismissDialog(){
        if(isDisplaying){
            navController.popBackStack()
            countDownTimer.cancel()
        } else countDownTimer.cancel()
    }
}