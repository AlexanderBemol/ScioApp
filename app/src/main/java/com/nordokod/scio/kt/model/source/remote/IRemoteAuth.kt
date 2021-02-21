package com.nordokod.scio.kt.model.source.remote

import com.nordokod.scio.kt.model.entity.User
import com.nordokod.scio.kt.utils.TaskResult

interface IRemoteAuth {
    suspend fun signInWithMail(email: String, password: String): TaskResult<User>
    suspend fun signUpWithMail(email: String, password: String): TaskResult<User>
    suspend fun signInWithGoogle(token: String): TaskResult<User>
    suspend fun signInWithFacebook(token: String): TaskResult<User>
    fun getBasicUserInfo(): TaskResult<User>
    fun isUserLogged(): TaskResult<Boolean>
    fun logOut(): TaskResult<Unit>
    suspend fun sendVerificationMail(): TaskResult<Unit>
    suspend fun refreshUser(): TaskResult<User>
}