package com.nordokod.scio.kt.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nordokod.scio.kt.constants.enums.GuideCategory
import com.nordokod.scio.kt.constants.enums.SyncState
import com.nordokod.scio.kt.utils.DateSerializer
import kotlinx.serialization.*
import java.util.*

@Entity
data class Guide(
        @PrimaryKey(autoGenerate = true) val id: Int =0,
        var remoteId: String = "",
        var topic: String = "",
        var category: Int = GuideCategory.OTHERS.code,
        var testDate: Date = Date(),
        var creationDate: Date = Date(),
        var updateDate: Date = Date(),
        var creationUser: String = "",
        var updateUser: String = "",
        var syncState: Int = SyncState.ONLY_IN_LOCAL.code,
        var lastSync: Date = Date()
)