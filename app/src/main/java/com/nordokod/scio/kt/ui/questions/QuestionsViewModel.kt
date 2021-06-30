package com.nordokod.scio.kt.ui.questions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.model.entity.GuideWithQuestions
import com.nordokod.scio.kt.model.repository.IQuestionRepository

class QuestionsViewModel(
        private val questionRepository: IQuestionRepository,
        private val idGuide: Int
) : ViewModel() {
    val error = MutableLiveData<Exception>()
    //val successMessage = MutableLiveData<SuccessMessage>()
    val guideWithQuestions = MutableLiveData<GuideWithQuestions>()

    fun getQuestions(){

    }
    


}