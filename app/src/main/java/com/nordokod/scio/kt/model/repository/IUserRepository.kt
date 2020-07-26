package com.nordokod.scio.kt.model.repository

import android.graphics.drawable.Drawable
import com.nordokod.scio.kt.model.entity.User
import com.nordokod.scio.kt.utils.TaskResult
import java.io.File

interface IUserRepository {
    suspend fun saveUserData(user: User): TaskResult<Unit>
    suspend fun getUserData(uid: String): TaskResult<User>
    suspend fun saveUserPhoto(photo: File): TaskResult<Unit>
    suspend fun getUserPhoto(uid: String): TaskResult<Drawable>
}