package com.nordokod.scio.kt.model.source.remote

import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.model.entity.Question
import com.nordokod.scio.kt.model.entity.QuestionWithAnswers
import com.nordokod.scio.kt.utils.TaskResult

interface IRemoteQuestion {
    suspend fun addQuestion(question: QuestionWithAnswers, guide: Guide) : TaskResult<Question>
    suspend fun updateQuestion(question: QuestionWithAnswers, guide: Guide) : TaskResult<Unit>
    suspend fun deleteQuestion(question: Question, guide: Guide) : TaskResult<Unit>
    suspend fun getGuideQuestions(guide: Guide) : TaskResult<List<QuestionWithAnswers>>
}