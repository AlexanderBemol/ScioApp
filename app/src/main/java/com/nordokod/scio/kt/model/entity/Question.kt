package com.nordokod.scio.kt.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nordokod.scio.kt.constants.enums.KindOfQuestion
import com.nordokod.scio.kt.constants.enums.SyncState

@Entity
data class Question (
        @PrimaryKey(autoGenerate = true) var id : Long = 0,
        var remoteId: String = "",
        var question: String = "",
        var idGuide: Int = 0,
        var kindOfQuestion: Int = KindOfQuestion.MULTIPLE_CHOICE.code,
        var syncState: Int = SyncState.ONLY_IN_LOCAL.code
)