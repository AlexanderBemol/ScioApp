package com.nordokod.scio.kt.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.nordokod.scio.R

class CreateView : Fragment() {
    private val navController by lazy { this.activity?.let { Navigation.findNavController(it,R.id.mainNav) } }
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_view, container, false)
    }

    override fun onStart() {
        super.onStart()
        navController!!.navigate(R.id.action_global_newGuideView)
    }
}