package com.nordokod.scio.kt.model.source.local

import androidx.room.*
import com.nordokod.scio.kt.model.entity.Answer
import com.nordokod.scio.kt.model.entity.GuideWithQuestions
import com.nordokod.scio.kt.model.entity.Question

@Dao
interface QuestionDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuestion(question: Question) : Long

    @Delete
    fun deleteQuestion(question: Question)

    @Update
    fun updateQuestion(question: Question)

    @Query("UPDATE Question SET syncState = :syncState WHERE id = :id")
    fun updateQuestionSyncState(id: Long, syncState: Int)

    @Query("SELECT * FROM Guide where id=:id")
    fun getGuideQuestions(id: Int): GuideWithQuestions

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnswer(answer: Answer.OpenAnswer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnswer(answer: Answer.TrueFalseAnswer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnswer(answer: Answer.MultipleChoiceAnswer)

    @Delete
    fun deleteAnswer(answer: Answer.OpenAnswer)

    @Delete
    fun deleteAnswer(answer: Answer.TrueFalseAnswer)

    @Query("DELETE FROM MultipleChoiceAnswer WHERE idQuestion = :idQuestion")
    fun deleteMultipleChoiceAnswers(idQuestion: Long)

    @Update
    fun updateAnswer(answer: Answer.OpenAnswer)

    @Update
    fun updateAnswer(answer: Answer.TrueFalseAnswer)

}