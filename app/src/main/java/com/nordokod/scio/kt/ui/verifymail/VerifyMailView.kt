package com.nordokod.scio.kt.ui.verifymail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.nordokod.scio.R
import com.nordokod.scio.kt.constants.enums.SendoScreen
import com.nordokod.scio.kt.utils.AnalyticsHelper
import com.nordokod.scio.kt.utils.getEnumErrorMessage
import kotlinx.android.synthetic.main.fragment_verify_mail_view.*
import org.koin.android.viewmodel.ext.android.viewModel

class VerifyMailView : Fragment() {
    private val viewModel by viewModel<VerifyMailViewModel>()
    private val navController : NavController by lazy { findNavController()}
    override fun onStart() {
        super.onStart()
        AnalyticsHelper.recordScreenView(SendoScreen.VERIFY_MAIL_SCREEN,this::class.simpleName.toString())
        initListeners()
        observeLiveData()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_verify_mail_view, container, false)
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshUser()
    }

    fun initListeners(){
        VMail_Swipe.setOnRefreshListener {
            viewModel.refreshUser()
        }
        VMail_BTN_Resend.setOnClickListener {
            viewModel.sendVerificationMail()        }
        VMail_BTN_Logout.setOnClickListener {
            viewModel.logout()
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
                        VMail_Swipe.isRefreshing = false
                    }
            )
            viewModel.successMessage.observe(
                    viewLifecycleOwner,
                    Observer {
                        it.getContentIfNotHandled()
                                ?.showMessage(context)
                        VMail_Swipe.isRefreshing = false
                    }
            )
            viewModel.verifyMailAction.observe(
                    this,
                    Observer {
                        when(it.getContentIfNotHandled()){
                            VerifyMailActions.GO_TO_PERMISSIONS -> navController.navigate(R.id.action_verifyMailView_to_permissionsView)
                            VerifyMailActions.GO_TO_LOGIN -> navController.navigate(R.id.action_verifyMailView_to_loginView)
                            VerifyMailActions.STOP_REFRESH_ANIMATION -> VMail_Swipe.isRefreshing = false
                        }
                        VMail_Swipe.isRefreshing = false
                    }
            )
        }
    }

}