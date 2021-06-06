package com.nordokod.scio.kt.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nordokod.scio.R
import com.nordokod.scio.kt.constants.enums.SendoScreen
import com.nordokod.scio.kt.utils.AnalyticsHelper

class HomeView : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_view, container, false)
    }

    override fun onStart() {
        super.onStart()
        AnalyticsHelper.recordScreenView(SendoScreen.HOME_VIEW,this::class.simpleName.toString())
    }
}