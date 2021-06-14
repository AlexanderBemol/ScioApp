package com.nordokod.scio.kt.ui.delete

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nordokod.scio.R
import com.nordokod.scio.kt.constants.Generic
import com.nordokod.scio.kt.ui.guides.GuidesViewModel
import com.nordokod.scio.kt.utils.getEnumErrorMessage
import kotlinx.android.synthetic.main.dialog_delete_guide.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class DeleteGuideView : DialogFragment() {
    private val guidesViewModel by sharedViewModel<GuidesViewModel>()
    private val deleteGuideViewModel by viewModel<DeleteGuideViewModel>()
    private val args : DeleteGuideViewArgs by navArgs()
    private val navController : NavController by lazy { findNavController()}

    private var isDisplaying = false
    private val countDownTimer = object : CountDownTimer(Generic.BEFORE_LOADING_TIME, Generic.BEFORE_LOADING_TIME) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {
            isDisplaying = true
            navController.navigate(R.id.action_global_loadingView)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        observeLiveData()
        return inflater.inflate(R.layout.dialog_delete_guide , container)
    }

    override fun onStart() {
        super.onStart()
        DLT_BTN_Cancel.setOnClickListener {
            this.dismiss()
        }
        DLT_BTN_Delete.setOnClickListener {
            countDownTimer.start()
            deleteGuideViewModel.deleteGuide(args.guideId)
        }
    }

    private fun observeLiveData(){
        val context = this.context
        if(context!=null){
            deleteGuideViewModel.successMessage.observe(
                    viewLifecycleOwner,
                    Observer {
                        dismissLoading()
                        guidesViewModel.getGuides()
                        it.showMessage(context)
                        this.dismiss()
                    }
            )
            deleteGuideViewModel.error.observe(
                    viewLifecycleOwner,
                    Observer {
                        dismissLoading()
                        it.getEnumErrorMessage().showMessage(context)
                        this.dismiss()
                    }
            )
        }
    }

    private fun dismissLoading(){
        if(isDisplaying){
            navController.popBackStack()
            countDownTimer.cancel()
            isDisplaying = false
        } else countDownTimer.cancel()
    }
}