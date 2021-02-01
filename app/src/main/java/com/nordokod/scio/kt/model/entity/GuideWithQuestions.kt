package com.nordokod.scio.kt.model.entity

import androidx.room.Embedded
import androidx.room.Relation

data class GuideWithQuestions(
        @Embedded val guide: Guide,
        @Relation(
                parentColumn = "id",
                entityColumn = "idGuide",
                entity = Question::class
        )
        val questions: List<QuestionWithAnswers> = listOf()
)