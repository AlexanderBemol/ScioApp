package com.nordokod.scio.kt.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nordokod.scio.kt.constants.enums.Provider
import com.nordokod.scio.kt.constants.enums.UserState
import java.util.*

@Entity
data class User(
        @PrimaryKey var uid: String = "",
        var emailVerified: Boolean = false,
        var newUser: Boolean = false,
        var displayName: String = "",
        var email: String = "",
        var photoURL: String = "",
        var provider: Int = Provider.MAIL.code,
        var userState: Int = UserState.FREE.code,
        var creationDate: Date = Date(),
        var synchronized: Boolean = false,
        var lastSync: Date = Date()
)