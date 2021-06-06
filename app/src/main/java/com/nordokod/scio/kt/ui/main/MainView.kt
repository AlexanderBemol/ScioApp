package com.nordokod.scio.kt.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.nordokod.scio.R
import com.nordokod.scio.kt.utils.getEnumErrorMessage
import kotlinx.android.synthetic.main.fragment_main_view.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainView : Fragment() {
    private val mainViewModel by viewModel<MainViewModel>()
    private val mainNavController by lazy { this.activity?.let { Navigation.findNavController(it,R.id.mainNav) } }
    private val appNavController : NavController by lazy { findNavController()}
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_view, container, false)
    }

    override fun onStart() {
        super.onStart()
        setUpNavigation()
        setListeners()
        observeLiveData()
    }

    private fun setListeners() {
        BTN_Logout.setOnClickListener {
            run {
                mainViewModel.logOut()
            }
        }
        BTN_Dark.setOnClickListener {
            activity?.setTheme(R.style.DefaultTheme)
        }
    }
    private fun observeLiveData() {
        val context = this.context
        if (context != null) {
            mainViewModel.error.observe(
                    viewLifecycleOwner,
                    Observer {
                        it.getContentIfNotHandled()
                                ?.getEnumErrorMessage()
                                ?.showMessage(context)
                    }
            )
            mainViewModel.successMessage.observe(
                    viewLifecycleOwner,
                    Observer {
                        it.getContentIfNotHandled()
                                ?.showMessage(context)
                    }
            )
            mainViewModel.mainAction.observe(
                    this,
                    Observer {
                        if (it.getContentIfNotHandled() == MainActions.GO_TO_LOGIN) {
                            appNavController.navigate(R.id.action_global_loginView)
                        }
                    }
            )
        }
    }

    private fun setUpNavigation() {
        bottom_navigation_view.visibility = View.VISIBLE
        mainNavController?.let {
            NavigationUI.setupWithNavController(bottom_navigation_view, mainNavController!!)
            NavigationUI.setupWithNavController(NAV_Menu,appNavController)
        }
    }

}