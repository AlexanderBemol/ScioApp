package com.nordokod.scio.kt.model.repository

import com.nordokod.scio.entity.NoQuestionsInGuide
import com.nordokod.scio.kt.constants.Generic
import com.nordokod.scio.kt.constants.UnknownException
import com.nordokod.scio.kt.constants.enums.KindOfQuestion
import com.nordokod.scio.kt.constants.enums.SyncState
import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.model.entity.Question
import com.nordokod.scio.kt.model.entity.QuestionWithAnswers
import com.nordokod.scio.kt.model.source.local.QuestionDAO
import com.nordokod.scio.kt.model.source.remote.RemoteQuestion
import com.nordokod.scio.kt.utils.NetworkManager
import com.nordokod.scio.kt.utils.TaskResult
import kotlinx.coroutines.withTimeout

class QuestionRepository(
        val guide: Guide,
        val remoteQuestion: RemoteQuestion,
        val localQuestion: QuestionDAO
) : IQuestionRepository {

    override suspend fun addQuestion(questionWithAnswers: QuestionWithAnswers) : TaskResult<QuestionWithAnswers> {
        return try {
            if (NetworkManager.isOnline()) {
                withTimeout(Generic.TIMEOUT_VALUE) {
                    when (val result = remoteQuestion.addQuestion(questionWithAnswers, guide)) {
                        is TaskResult.Success -> {
                            questionWithAnswers.question = result.data
                            questionWithAnswers.question.syncState = SyncState.SYNCHRONIZED.code
                            addQuestionOffline(questionWithAnswers)
                        }
                        is TaskResult.Error -> {
                            questionWithAnswers.question.syncState = SyncState.ONLY_IN_LOCAL.code
                            addQuestionOffline(questionWithAnswers)
                        }
                    }
                }
            } else {
                questionWithAnswers.question.syncState = SyncState.ONLY_IN_LOCAL.code
                addQuestionOffline(questionWithAnswers)
            }
        } catch (e: Exception) {
            questionWithAnswers.question.syncState = SyncState.ONLY_IN_LOCAL.code
            addQuestionOffline(questionWithAnswers)
        }
    }

    override suspend fun updateQuestion(questionWithAnswers: QuestionWithAnswers): TaskResult<Unit> {
        return try {
            if (NetworkManager.isOnline()) {
                withTimeout(Generic.TIMEOUT_VALUE) {
                    when (remoteQuestion.updateQuestion(questionWithAnswers, guide)) {
                        is TaskResult.Success -> {
                            questionWithAnswers.question.syncState = SyncState.SYNCHRONIZED.code
                            updateQuestionOffline(questionWithAnswers)
                        }
                        is TaskResult.Error -> {
                            questionWithAnswers.question.syncState = SyncState.UPDATED_IN_LOCAL.code
                            updateQuestionOffline(questionWithAnswers)
                        }
                    }
                }
            } else {
                questionWithAnswers.question.syncState = SyncState.UPDATED_IN_LOCAL.code
                updateQuestionOffline(questionWithAnswers)
            }
        } catch (e: Exception) {
            questionWithAnswers.question.syncState = SyncState.UPDATED_IN_LOCAL.code
            updateQuestionOffline(questionWithAnswers)
        }
    }

    override suspend fun deleteQuestion(questionWithAnswers: QuestionWithAnswers): TaskResult<Unit> {
        return try {
            if (NetworkManager.isOnline()) {
                withTimeout(Generic.TIMEOUT_VALUE) {
                    when (remoteQuestion.deleteQuestion(questionWithAnswers.question, guide)) {
                        is TaskResult.Success -> {
                            localQuestion.deleteQuestion(questionWithAnswers.question)
                            when(questionWithAnswers.question.kindOfQuestion){
                                KindOfQuestion.OPEN.code -> {
                                    localQuestion.deleteAnswer(questionWithAnswers.openAnswer)
                                }
                                KindOfQuestion.TRUE_FALSE.code -> {
                                    localQuestion.deleteAnswer(questionWithAnswers.trueFalseAnswer)
                                }
                                KindOfQuestion.MULTIPLE_CHOICE.code -> {
                                    questionWithAnswers.multipleChoiceAnswers.forEach{
                                        localQuestion.deleteAnswer(it)
                                    }
                                }
                            }
                            TaskResult.Success(Unit)
                        }
                        is TaskResult.Error -> {
                            questionWithAnswers.question.syncState = SyncState.DELETED_IN_LOCAL.code
                            localQuestion.updateQuestionSyncState(questionWithAnswers.question.id,SyncState.DELETED_IN_LOCAL.code)
                            TaskResult.Success(Unit)
                        }
                    }
                }
            } else {
                questionWithAnswers.question.syncState = SyncState.DELETED_IN_LOCAL.code
                localQuestion.updateQuestionSyncState(questionWithAnswers.question.id,SyncState.DELETED_IN_LOCAL.code)
                TaskResult.Success(Unit)
            }
        } catch (e: Exception) {
            TaskResult.Error(e)
        }
    }

    override suspend fun getGuideQuestions(): TaskResult<ArrayList<QuestionWithAnswers>> {
        return try{
            val guideQuestions = localQuestion.getGuideQuestions(guide.id)
            if(guideQuestions.questions.isNotEmpty()) TaskResult.Success(guideQuestions.questions)
            else TaskResult.Error(NoQuestionsInGuide())
        } catch (e : Exception){
            TaskResult.Error(e)
        }
    }

    private fun addQuestionOffline(questionWithAnswers: QuestionWithAnswers): TaskResult<QuestionWithAnswers> {
        return try {
            val id = localQuestion.insertQuestion(questionWithAnswers.question) //get id
            questionWithAnswers.question.id = id
            when (questionWithAnswers.question.kindOfQuestion) {
                KindOfQuestion.MULTIPLE_CHOICE.code -> {
                    questionWithAnswers.multipleChoiceAnswers.forEach {
                        it.idQuestion = id
                        localQuestion.insertAnswer(it)
                    }
                }
                KindOfQuestion.TRUE_FALSE.code -> {
                    questionWithAnswers.trueFalseAnswer.let {
                        it.idQuestion = id
                        localQuestion.insertAnswer(it)
                    }
                }
                KindOfQuestion.OPEN.code -> {
                    questionWithAnswers.openAnswer.let {
                        it.idQuestion = id
                        localQuestion.insertAnswer(it)
                    }
                }
            }
            TaskResult.Success(questionWithAnswers)
        } catch (e: Exception) {
            TaskResult.Error(e)
        }
    }

    private fun updateQuestionOffline(questionWithAnswers: QuestionWithAnswers) : TaskResult<Unit>{
        return try{
            localQuestion.updateQuestion(questionWithAnswers.question)
            when(questionWithAnswers.question.syncState){
                KindOfQuestion.OPEN.code ->{
                    localQuestion.updateAnswer(questionWithAnswers.openAnswer)
                }
                KindOfQuestion.TRUE_FALSE.code ->{
                    localQuestion.updateAnswer(questionWithAnswers.trueFalseAnswer)
                }
                KindOfQuestion.MULTIPLE_CHOICE.code ->{
                    localQuestion.deleteMultipleChoiceAnswers(questionWithAnswers.question.id)
                    questionWithAnswers.multipleChoiceAnswers.forEach {
                        localQuestion.insertAnswer(it)
                    }
                }
            }
            TaskResult.Success(Unit)
        } catch (e : Exception) {
            TaskResult.Error(e)
        }
    }
}