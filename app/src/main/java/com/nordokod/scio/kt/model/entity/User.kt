package com.nordokod.scio.kt.model.entity

import java.util.*

data class User(
        val uid: String,
        val emailVerified: Boolean,
        val newUser: Boolean,
        val displayName: String,
        val email: String,
        val photoURL: String,
        val provider: Int,
        val userState: Int,
        val creationDate: Date
)