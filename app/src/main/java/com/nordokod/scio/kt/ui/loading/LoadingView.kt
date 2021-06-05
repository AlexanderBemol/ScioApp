package com.nordokod.scio.kt.ui.loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.nordokod.scio.R
import kotlinx.android.synthetic.main.dialog_loading.*


class LoadingView : DialogFragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_loading , container)
    }

    override fun onStart() {
        super.onStart()
        this.dialog?.setCanceledOnTouchOutside(false)
        DLoading_Icon.startAnimation(
                AnimationUtils.loadAnimation(this.context,R.anim.rotate)
        )
    }
}