package com.nordokod.scio.kt.model.entity

data class MultipleChoiceQuestion(
        val question: String,
        val id: Int,
        val answer: String,
        val correct: Boolean
)