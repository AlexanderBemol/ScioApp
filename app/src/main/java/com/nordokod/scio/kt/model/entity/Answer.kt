package com.nordokod.scio.kt.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

sealed class Answer {
    @Entity
    data class OpenAnswer(
            @PrimaryKey(autoGenerate = true) var id: Long = 0,
            var idQuestion: Long = 0,
            var answer: String = ""
    ) : Answer()

    @Entity
    data class TrueFalseAnswer(
            @PrimaryKey(autoGenerate = true) var id: Long = 0,
            var idQuestion: Long = 0,
            var answer: Boolean = true
    ) : Answer()

    @Entity
    data class MultipleChoiceAnswer(
            @PrimaryKey(autoGenerate = true) var id: Long = 0,
            var idQuestion: Long = 0,
            var answer: String = "",
            var isCorrect: Boolean = true
    ) : Answer()
}