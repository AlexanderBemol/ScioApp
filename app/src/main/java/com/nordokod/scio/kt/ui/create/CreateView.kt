package com.nordokod.scio.kt.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.nordokod.scio.R
import com.nordokod.scio.kt.constants.enums.SendoScreen
import com.nordokod.scio.kt.utils.AnalyticsHelper

class CreateView : Fragment() {
    private val navController by lazy { this.activity?.let { Navigation.findNavController(it,R.id.mainNav) } }
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        navController!!.navigate(R.id.action_global_newGuideView)
        return inflater.inflate(R.layout.fragment_create_view, container, false)
    }

    override fun onResume() {
        super.onResume()
        AnalyticsHelper.recordScreenView(SendoScreen.CREATE_GUIDE_VIEW,this::class.simpleName.toString())
    }
}