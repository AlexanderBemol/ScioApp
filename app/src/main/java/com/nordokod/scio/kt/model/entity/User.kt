package com.nordokod.scio.kt.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity
data class User(
        @PrimaryKey val uid: String,
        val emailVerified: Boolean,
        val newUser: Boolean,
        val displayName: String,
        val email: String,
        val photoURL: String,
        val provider: Int,
        val userState: Int,
        val creationDate: Date,
        var synchronized: Boolean
)