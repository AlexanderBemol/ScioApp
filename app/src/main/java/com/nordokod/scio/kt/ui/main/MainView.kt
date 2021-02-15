package com.nordokod.scio.kt.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.nordokod.scio.R
import kotlinx.android.synthetic.main.fragment_main_view.*

class MainView : Fragment() {
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
    }

    private fun setListeners() {
        BTN_Logout.setOnClickListener {
            run {
                appNavController.navigate(R.id.action_global_loginView)

            }
        }
    }

    private fun setUpNavigation() {
        mainNavController?.let {
            NavigationUI.setupWithNavController(bottom_navigation_view, mainNavController!!)
        }

    }

}