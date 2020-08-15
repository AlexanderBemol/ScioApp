package com.nordokod.scio.kt.model.entity

sealed class Answer {
    data class OpenAnswer(
            var idOpenAnswer: Int,
            var idQuestion: Int,
            var anser: String
    ) : Answer()
    data class TrueFalseAnswer(
            var idOpenAnswer: Int,
            var idQuestion: Int,
            var anser: Boolean
    ) : Answer()
    data class MultipleChoiceAnswer(
            var idOpenAnswer: Int,
            var idQuestion: Int,
            var anser: String,
            var isCorrect: String
    )
}