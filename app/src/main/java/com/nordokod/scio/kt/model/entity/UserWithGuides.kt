package com.nordokod.scio.kt.model.entity

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithGuides(
        @Embedded val user: User,
        @Relation(
                parentColumn = "uid",
                entityColumn = "updateUser"
        )
        val guides: List<Guide>
)