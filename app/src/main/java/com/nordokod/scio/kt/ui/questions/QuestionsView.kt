package com.nordokod.scio.kt.ui.questions

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.nordokod.scio.R
import com.nordokod.scio.kt.constants.Generic
import com.nordokod.scio.kt.constants.enums.SendoScreen
import com.nordokod.scio.kt.utils.AnalyticsHelper
import org.koin.android.viewmodel.ext.android.viewModel

class QuestionsView : Fragment() {
    private val args : QuestionsViewArgs by navArgs()
    private val idGuide by lazy { args.idGuide }
    private val questionsViewModel by viewModel<QuestionsViewModel>()

    private var isDisplaying = false
    private val countDownTimer = object : CountDownTimer(Generic.BEFORE_SHORT_LOADING_TIME, Generic.BEFORE_SHORT_LOADING_TIME) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {
            isDisplaying = true
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        observeLiveData()
        return inflater.inflate(R.layout.fragment_questions_view, container, false)
    }

    override fun onResume() {
        super.onResume()
        AnalyticsHelper.recordScreenView(SendoScreen.QUESTIONS_SCREEN, this::class.simpleName.toString())
    }

    override fun onStart() {
        super.onStart()


    }

    private fun observeLiveData() {

    }
}