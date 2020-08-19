package com.nordokod.scio.kt.model.entity

import androidx.room.Embedded
import androidx.room.Relation

data class QuestionWithAnswers(
        @Embedded val question: Question,
        @Relation(parentColumn = "id",entityColumn = "idQuestion")
        val answers: List<Answer>
)