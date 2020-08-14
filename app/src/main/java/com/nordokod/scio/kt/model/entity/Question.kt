package com.nordokod.scio.kt.model.entity

data class Question (
        val id: String,
        val question: String,
        val idGuide: String,
        val kindOfQuestion: Int
)