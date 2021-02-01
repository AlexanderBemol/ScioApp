package com.nordokod.scio.kt.model.source.remote

import android.graphics.Bitmap
import com.nordokod.scio.kt.model.entity.User
import com.nordokod.scio.kt.utils.TaskResult
import java.io.File

interface IRemoteUser{
    suspend fun setUserDocument(user: User): TaskResult<Unit>
    suspend fun getUserDocument(uid: String): TaskResult<User>
    suspend fun saveUserPhoto(photo: File): TaskResult<Unit>
    suspend fun getUserFirebasePhoto(uid: String): TaskResult<Bitmap>
    fun getUserExternalPhoto(user: User): TaskResult<Bitmap>
}