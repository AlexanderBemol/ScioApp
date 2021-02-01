package com.nordokod.scio.kt.model.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

data class QuestionWithAnswers(
        @Embedded var question: Question,
        @Relation(parentColumn = "id",entityColumn = "idQuestion")
        val openAnswer: Answer.OpenAnswer? = Answer.OpenAnswer(),
        @Relation(parentColumn = "id",entityColumn = "idQuestion")
        val trueFalseAnswer: Answer.TrueFalseAnswer? = Answer.TrueFalseAnswer(),
        @Relation(parentColumn = "id",entityColumn = "idQuestion")
        val multipleChoiceAnswers: List<Answer.MultipleChoiceAnswer>? = listOf()

)