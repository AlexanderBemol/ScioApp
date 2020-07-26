package com.nordokod.scio.kt.model.entity

import java.util.*

data class Guide(
        val id: String,
        val topic: String,
        val category: Int,
        val testDate: Date,
        val creationDate: Date,
        val updateDate: Date,
        val creationUser: String,
        val updateUser: String
)