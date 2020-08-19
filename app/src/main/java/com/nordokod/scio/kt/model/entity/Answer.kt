package com.nordokod.scio.kt.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

sealed class Answer {
    @Entity
    data class OpenAnswer(
            @PrimaryKey(autoGenerate = true) var idOpenAnswer: Int,
            var idQuestion: Int,
            var answer: String
    ) : Answer()
    @Entity
    data class TrueFalseAnswer(
            @PrimaryKey(autoGenerate = true) var idOpenAnswer: Int,
            var idQuestion: Int,
            var answer: Boolean
    ) : Answer()
    @Entity
    data class MultipleChoiceAnswer(
            @PrimaryKey(autoGenerate = true) var idOpenAnswer: Int,
            var idQuestion: Int,
            var answer: String,
            var isCorrect: String
    ) : Answer()
}