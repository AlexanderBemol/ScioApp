package com.nordokod.scio.kt.ui.splash

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.nordokod.scio.R
import com.nordokod.scio.kt.constants.Generic
import org.koin.android.viewmodel.ext.android.viewModel
import com.nordokod.scio.kt.ui.splash.SplashActions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashView : Fragment() {
    private val viewModel by viewModel<SplashViewModel>()
    private val navController : NavController by lazy { findNavController()}
    override fun onStart() {
        super.onStart()
        observeLiveData()
        viewModel.loadInitialConfiguration()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash_view, container, false)
    }

    private fun observeLiveData(){
        val context = this.context
        if(context != null){
            viewModel.splashAction.observe(
                    viewLifecycleOwner,
                    Observer { action ->
                        GlobalScope.launch {
                            delay(Generic.SPLASH_SCREEN_TIME)
                            navController.navigate(R.id.action_splashView_to_loginView)
                        }
                    }
            )
        }
    }
}