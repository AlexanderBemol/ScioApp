package com.nordokod.scio.kt.model.repository

import com.nordokod.scio.kt.model.entity.QuestionWithAnswers
import com.nordokod.scio.kt.utils.TaskResult

interface IQuestionRepository {
    suspend fun addQuestion(questionWithAnswers: QuestionWithAnswers) : TaskResult<QuestionWithAnswers>
    suspend fun updateQuestion(questionWithAnswers: QuestionWithAnswers) : TaskResult<Unit>
    suspend fun deleteQuestion(questionWithAnswers: QuestionWithAnswers) : TaskResult<Unit>
    suspend fun getGuideQuestions() : TaskResult<List<QuestionWithAnswers>>
    suspend fun syncInRemote() : TaskResult<Unit>
    suspend fun syncInLocal() : TaskResult<Unit>
}