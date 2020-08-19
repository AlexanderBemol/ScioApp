package com.nordokod.scio.kt.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Question (
        @PrimaryKey(autoGenerate = true) var id: Int,
        var remoteId: String,
        var question: String,
        var idGuide: Int,
        var kindOfQuestion: Int
)