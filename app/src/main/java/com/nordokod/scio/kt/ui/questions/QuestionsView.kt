package com.nordokod.scio.kt.ui.questions

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nordokod.scio.R
import com.nordokod.scio.kt.constants.Generic

class QuestionsView : Fragment() {
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

    private fun observeLiveData() {

    }
}