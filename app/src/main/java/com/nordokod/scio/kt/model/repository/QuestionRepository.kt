package com.nordokod.scio.kt.model.repository

import com.nordokod.scio.kt.constants.Generic
import com.nordokod.scio.kt.constants.GuideException
import com.nordokod.scio.kt.constants.PhoneNetworkException
import com.nordokod.scio.kt.constants.enums.KindOfQuestion
import com.nordokod.scio.kt.constants.enums.SyncState
import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.model.entity.Question
import com.nordokod.scio.kt.model.entity.QuestionWithAnswers
import com.nordokod.scio.kt.model.source.local.QuestionDAO
import com.nordokod.scio.kt.model.source.remote.IRemoteQuestion
import com.nordokod.scio.kt.utils.NetworkManager
import com.nordokod.scio.kt.utils.TaskResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

class QuestionRepository(
        private val guide: Guide,
        private val remoteQuestion: IRemoteQuestion,
        private val localQuestion: QuestionDAO
) : IQuestionRepository {

    override suspend fun addQuestion(questionWithAnswers: QuestionWithAnswers): TaskResult<QuestionWithAnswers> {
        return if (NetworkManager.isOnline()) {
            try {
                withContext(Dispatchers.IO){
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
                }
            } catch (e: Exception) {
                questionWithAnswers.question.syncState = SyncState.ONLY_IN_LOCAL.code
                addQuestionOffline(questionWithAnswers)
            }
        } else {
            questionWithAnswers.question.syncState = SyncState.ONLY_IN_LOCAL.code
            addQuestionOffline(questionWithAnswers)
        }
    }

    override suspend fun updateQuestion(questionWithAnswers: QuestionWithAnswers): TaskResult<Unit> {
        return if (NetworkManager.isOnline()) {
            try {
                withContext(Dispatchers.IO){
                    withTimeout(Generic.TIMEOUT_VALUE) {
                        val result =
                                if(questionWithAnswers.question.syncState == SyncState.ONLY_IN_LOCAL.code)
                                    remoteQuestion.addQuestion(questionWithAnswers,guide)
                                else
                                    remoteQuestion.updateQuestion(questionWithAnswers, guide)
                        when (result) {
                            is TaskResult.Success -> {
                                if(questionWithAnswers.question.syncState == SyncState.ONLY_IN_LOCAL.code)
                                    questionWithAnswers.question.remoteId = (result.data as Question).remoteId
                                questionWithAnswers.question.syncState = SyncState.SYNCHRONIZED.code
                                updateQuestionOffline(questionWithAnswers)
                            }
                            is TaskResult.Error -> {
                                if(questionWithAnswers.question.syncState != SyncState.ONLY_IN_LOCAL.code)
                                    questionWithAnswers.question.syncState = SyncState.UPDATED_IN_LOCAL.code
                                updateQuestionOffline(questionWithAnswers)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                if(questionWithAnswers.question.syncState != SyncState.ONLY_IN_LOCAL.code)
                    questionWithAnswers.question.syncState = SyncState.UPDATED_IN_LOCAL.code
                updateQuestionOffline(questionWithAnswers)
            }
        } else {
            if(questionWithAnswers.question.syncState != SyncState.ONLY_IN_LOCAL.code)
                questionWithAnswers.question.syncState = SyncState.UPDATED_IN_LOCAL.code
            updateQuestionOffline(questionWithAnswers)
        }
    }

    override suspend fun deleteQuestion(questionWithAnswers: QuestionWithAnswers): TaskResult<Unit> {
        return if (NetworkManager.isOnline()) {
            try {
                withContext(Dispatchers.IO){
                    withTimeout(Generic.TIMEOUT_VALUE) {
                        when (remoteQuestion.deleteQuestion(questionWithAnswers.question, guide)) {
                            is TaskResult.Success -> {
                                deleteQuestionOffline(questionWithAnswers)
                            }
                            is TaskResult.Error -> {
                                questionWithAnswers.question.syncState = SyncState.DELETED_IN_LOCAL.code
                                updateQuestionOffline(questionWithAnswers)
                            }
                        }
                    }
                }
            } catch (e: Exception){
                if(questionWithAnswers.question.syncState == SyncState.ONLY_IN_LOCAL.code)
                    deleteQuestionOffline(questionWithAnswers)
                else{
                    questionWithAnswers.question.syncState = SyncState.DELETED_IN_LOCAL.code
                    updateQuestionOffline(questionWithAnswers)
                    TaskResult.Success(Unit)
                }
            }
        } else {
            if(questionWithAnswers.question.syncState == SyncState.ONLY_IN_LOCAL.code)
                deleteQuestionOffline(questionWithAnswers)
            else{
                questionWithAnswers.question.syncState = SyncState.DELETED_IN_LOCAL.code
                updateQuestionOffline(questionWithAnswers)
                TaskResult.Success(Unit)
            }
        }
    }

    override suspend fun getGuideQuestions(): TaskResult<List<QuestionWithAnswers>> {
        return try {
            withContext(Dispatchers.IO){
                val guideQuestions = localQuestion.getGuideQuestions(guide.id)
                if (guideQuestions.questions.isNotEmpty())
                    TaskResult.Success(guideQuestions.questions.filter {
                        q -> q.question.syncState != SyncState.DELETED_IN_LOCAL.code
                    })
                else
                    TaskResult.Error(GuideException(GuideException.Code.NO_QUESTIONS_IN_GUIDE))
            }

        } catch (e: Exception) {
            TaskResult.Error(e)
        }
    }

    override suspend fun syncInRemote(): TaskResult<Unit> {
        return try {
            if (NetworkManager.isOnline()) {
                val guideWithQuestions = localQuestion.getGuideQuestions(guide.id)
                guideWithQuestions.questions.forEach {
                    when (it.question.syncState) {
                        SyncState.ONLY_IN_LOCAL.code -> {
                            withTimeout(Generic.TIMEOUT_VALUE) {
                                val result = remoteQuestion.addQuestion(it, guide)
                                if (result is TaskResult.Success){
                                    val q = it.question
                                    q.syncState = SyncState.SYNCHRONIZED.code
                                    localQuestion.updateQuestion(q)
                                }
                            }
                        }
                        SyncState.UPDATED_IN_LOCAL.code -> {
                            withTimeout(Generic.TIMEOUT_VALUE) {
                                if (remoteQuestion.updateQuestion(it, guide) is TaskResult.Success)
                                    localQuestion.updateQuestionSyncState(it.question.id, SyncState.SYNCHRONIZED.code)
                            }
                        }
                        SyncState.DELETED_IN_LOCAL.code -> {
                            if (remoteQuestion.deleteQuestion(it.question, guide) is TaskResult.Success)
                                localQuestion.deleteQuestion(it.question)

                        }
                    }
                }
                TaskResult.Success(Unit)
            } else {
                TaskResult.Error(PhoneNetworkException())
            }
        } catch (e: Exception) {
            TaskResult.Error(e)
        }
    }

    override suspend fun syncInLocal(): TaskResult<Unit> {
        return try{
            if (NetworkManager.isOnline()) {
                withTimeout(Generic.TIMEOUT_VALUE){
                    when(val result = remoteQuestion.getGuideQuestions(guide)){
                        is TaskResult.Success -> {
                            result.data.forEach{
                                it.question.idGuide = guide.id
                                addQuestionOffline(it)
                            }
                            TaskResult.Success(Unit)
                        }
                        is TaskResult.Error -> {
                            result
                        }
                    }
                }
            } else
                TaskResult.Error(PhoneNetworkException())
        } catch (e: Exception) {
            TaskResult.Error(e)
        }
    }


    private fun addQuestionOffline(questionWithAnswers: QuestionWithAnswers): TaskResult<QuestionWithAnswers> {
        return try {
            val id = localQuestion.insertQuestion(questionWithAnswers.question) //get id
            questionWithAnswers.question.id = id
            when (questionWithAnswers.question.kindOfQuestion) {
                KindOfQuestion.MULTIPLE_CHOICE.code -> {
                    questionWithAnswers.multipleChoiceAnswers!!.forEach {
                        it.idQuestion = id
                        localQuestion.insertAnswer(it)
                    }
                }
                KindOfQuestion.TRUE_FALSE.code -> {
                    questionWithAnswers.trueFalseAnswer.let {
                        it!!.idQuestion = id
                        localQuestion.insertAnswer(it)
                    }
                }
                KindOfQuestion.OPEN.code -> {
                    questionWithAnswers.openAnswer.let {
                        it!!.idQuestion = id
                        localQuestion.insertAnswer(it)
                    }
                }
            }
            TaskResult.Success(questionWithAnswers)
        } catch (e: Exception) {
            TaskResult.Error(e)
        }
    }

    private fun updateQuestionOffline(questionWithAnswers: QuestionWithAnswers): TaskResult<Unit> {
        return try {
            localQuestion.updateQuestion(questionWithAnswers.question)
            when (questionWithAnswers.question.kindOfQuestion) {
                KindOfQuestion.OPEN.code -> {
                    questionWithAnswers.openAnswer?.let { localQuestion.updateAnswer(it) }
                }
                KindOfQuestion.TRUE_FALSE.code -> {
                    questionWithAnswers.trueFalseAnswer?.let { localQuestion.updateAnswer(it) }
                }
                KindOfQuestion.MULTIPLE_CHOICE.code -> {
                    localQuestion.deleteMultipleChoiceAnswers(questionWithAnswers.question.id)
                    questionWithAnswers.multipleChoiceAnswers?.forEach {
                        localQuestion.insertAnswer(it)
                    }
                }
            }
            TaskResult.Success(Unit)
        } catch (e: Exception) {
            TaskResult.Error(e)
        }
    }

    private fun deleteQuestionOffline(questionWithAnswers: QuestionWithAnswers): TaskResult<Unit> {
        return try {
            localQuestion.deleteQuestion(questionWithAnswers.question)
            when (questionWithAnswers.question.kindOfQuestion) {
                KindOfQuestion.OPEN.code -> {
                    questionWithAnswers.openAnswer?.let { localQuestion.deleteAnswer(it) }
                }
                KindOfQuestion.TRUE_FALSE.code -> {
                    questionWithAnswers.trueFalseAnswer?.let { localQuestion.deleteAnswer(it) }
                }
                KindOfQuestion.MULTIPLE_CHOICE.code -> {
                    localQuestion.deleteMultipleChoiceAnswers(questionWithAnswers.question.id)
                }
            }
            TaskResult.Success(Unit)
        } catch (e: Exception) {
            TaskResult.Error(e)
        }
    }
}