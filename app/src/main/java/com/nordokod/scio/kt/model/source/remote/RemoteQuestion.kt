package com.nordokod.scio.kt.model.source.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nordokod.scio.kt.constants.DataTags
import com.nordokod.scio.kt.constants.UnknownException
import com.nordokod.scio.kt.constants.enums.KindOfQuestion
import com.nordokod.scio.kt.model.entity.Answer
import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.model.entity.Question
import com.nordokod.scio.kt.model.entity.QuestionWithAnswers
import com.nordokod.scio.kt.utils.TaskResult
import kotlinx.coroutines.tasks.await
import kotlin.collections.ArrayList

class RemoteQuestion(
        private val firebaseFirestore: FirebaseFirestore,
        private val firebaseAuth: FirebaseAuth
) : IRemoteQuestion {

    /**
     * Add question to the guide
     * @param question : Question with the answer
     * @param guide : Guide with remoteID
     * @return TaskResult<Question> with the result
     */
    override suspend fun addQuestion(question: QuestionWithAnswers, guide: Guide) : TaskResult<Question> {
        val uid = firebaseAuth.uid
        val q = question.question
        return if (uid != null && guide.remoteId.isNotEmpty()) {
            try {
                val data = mutableMapOf<String,Any>(
                        Question::question.name to q.question,
                        Question::kindOfQuestion.name to q.kindOfQuestion
                )
                when (q.kindOfQuestion) {
                    KindOfQuestion.OPEN.code -> {
                        data[Answer.OpenAnswer::answer.name] = question.openAnswer!!.answer
                    }
                    KindOfQuestion.TRUE_FALSE.code -> {
                        data[Answer.TrueFalseAnswer::answer.name] = question.trueFalseAnswer!!.answer
                    }
                    KindOfQuestion.MULTIPLE_CHOICE.code -> {
                        val answers = mutableMapOf<String,Any>()
                        var i = 0
                        question.multipleChoiceAnswers?.forEach {
                            answers[i.toString()] = it
                            i++
                        }
                        data[Answer.MultipleChoiceAnswer::answer.name] = answers
                    }
                }
                val result = firebaseFirestore
                        .collection(DataTags.GUIDES_COLLECTION)
                        .document(uid)
                        .collection(DataTags.GUIDES_COLLECTION)
                        .document(guide.remoteId)
                        .collection(DataTags.QUESTIONS_COLLECTION)
                        .add(data)
                        .await()
                q.remoteId = result.id
                TaskResult.Success(q)
            }catch (e: Exception){
                TaskResult.Error(e)
            }
        }else{
            TaskResult.Error(UnknownException())
        }
    }

    /**
     * update a question
     * @param question : Question with remoteId
     * @param guide : Guide with remoteId
     * @return TaskResult<Unit> with the result
     */
    override suspend fun updateQuestion(question: QuestionWithAnswers, guide: Guide) : TaskResult<Unit>{
        val uid = firebaseAuth.uid
        val q = question.question
        return if (uid != null && guide.remoteId.isNotEmpty()) {
            try {
                val data = hashMapOf<String,Any>(
                        Question::question.name to q.question,
                        Question::kindOfQuestion.name to q.kindOfQuestion
                )
                when (question.question.kindOfQuestion) {
                    KindOfQuestion.OPEN.code -> {
                        data[Answer.OpenAnswer::answer.name] = question.openAnswer!!.answer
                    }
                    KindOfQuestion.TRUE_FALSE.code -> {
                        data[Answer.TrueFalseAnswer::answer.name] = question.trueFalseAnswer!!.answer
                    }
                    KindOfQuestion.MULTIPLE_CHOICE.code -> {
                        val answers = mutableMapOf<String,Any>()
                        var i = 0
                        question.multipleChoiceAnswers?.forEach {
                            answers[i.toString()] = it
                            i++
                        }
                        data[Answer.MultipleChoiceAnswer::answer.name] = answers
                    }
                }
                firebaseFirestore
                        .collection(DataTags.GUIDES_COLLECTION)
                        .document(uid)
                        .collection(DataTags.GUIDES_COLLECTION)
                        .document(guide.remoteId)
                        .collection(DataTags.QUESTIONS_COLLECTION)
                        .document(q.remoteId)
                        .set(data)
                        .await()
                TaskResult.Success(Unit)
            }catch (e: Exception){
                TaskResult.Error(e)
            }
        }else{
            TaskResult.Error(UnknownException())
        }
    }

    /**
     * delete a question
     * @param question : Question with remoteId
     * @param guide : Guide with remoteId
     * @return TaskResult<Unit> with the result
     */
    override suspend fun deleteQuestion(question: Question, guide: Guide) : TaskResult<Unit>{
        val uid = firebaseAuth.uid
        return if (uid != null && guide.remoteId.isNotEmpty()) {
            try {
                firebaseFirestore
                        .collection(DataTags.GUIDES_COLLECTION)
                        .document(uid)
                        .collection(DataTags.GUIDES_COLLECTION)
                        .document(guide.remoteId)
                        .collection(DataTags.QUESTIONS_COLLECTION)
                        .document(question.remoteId)
                        .delete()
                        .await()
                TaskResult.Success(Unit)
            }catch (e: Exception){
                TaskResult.Error(e)
            }
        }else{
            TaskResult.Error(UnknownException())
        }
    }

    /**
     * get the questions from a guide
     * @param guide : Guide with remoteId
     * @return TaskResult<ArrayList<QuestionsWithAnswers>> with the result
     */
    override suspend fun getGuideQuestions(guide: Guide) : TaskResult<List<QuestionWithAnswers>>{
        val uid = firebaseAuth.uid
        return if (uid != null && guide.remoteId.isNotEmpty()) {
            try {
                val result = firebaseFirestore
                        .collection(DataTags.GUIDES_COLLECTION)
                        .document(uid)
                        .collection(DataTags.GUIDES_COLLECTION)
                        .document(guide.remoteId)
                        .collection(DataTags.QUESTIONS_COLLECTION)
                        .get()
                        .await()
                val list = mutableListOf<QuestionWithAnswers>()
                result.documents.forEach{
                    val question = Question(
                            remoteId = it.id,
                            question = it[Question::question.name].toString(),
                            kindOfQuestion = (it.getLong(Question::kindOfQuestion.name) as Long).toInt()
                    )
                    when(question.kindOfQuestion){
                        KindOfQuestion.OPEN.code -> {
                            list.add(
                                    QuestionWithAnswers(
                                            question = question,
                                            openAnswer = Answer.OpenAnswer(answer = it[Answer.OpenAnswer::answer.name].toString())
                                    )
                            )
                        }
                        KindOfQuestion.TRUE_FALSE.code -> {
                            list.add(
                                    QuestionWithAnswers(
                                            question = question,
                                            trueFalseAnswer = Answer.TrueFalseAnswer(answer = it.getBoolean(Answer.TrueFalseAnswer::answer.name)!!)
                                    )
                            )
                        }
                        KindOfQuestion.MULTIPLE_CHOICE.code -> {
                            val answersMap  = it[Answer.MultipleChoiceAnswer::answer.name] as Map<String,Any>
                            val answers = mutableListOf<Answer.MultipleChoiceAnswer>()
                            for (i in 0 until answersMap.size){
                                val auxMap = answersMap[i.toString()] as Map<String, Any>
                                answers.add(
                                        Answer.MultipleChoiceAnswer(
                                                id = auxMap[Answer.MultipleChoiceAnswer::id.name] as Long,
                                                isCorrect = auxMap["correct"] as Boolean,
                                                answer = auxMap[Answer.MultipleChoiceAnswer::answer.name] as String,
                                                idQuestion = auxMap[Answer.MultipleChoiceAnswer::idQuestion.name] as Long
                                        )
                                )
                            }
                            list.add(
                                    QuestionWithAnswers(
                                            question = question,
                                            multipleChoiceAnswers = answers
                                    )
                            )
                        }
                    }
                }
                TaskResult.Success(list)
            }catch (e: Exception){
                TaskResult.Error(e)
            }
        }else{
            TaskResult.Error(UnknownException())
        }
    }

}