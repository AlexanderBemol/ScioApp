package com.nordokod.scio.kt.model.entity

data class Question (
        val id: String,
        val question: String,
        val guide: String,
        val kindOfQuestion: Int
)