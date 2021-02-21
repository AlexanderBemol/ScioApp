package com.nordokod.scio.kt.model.repository

import com.nordokod.scio.kt.model.entity.User
import com.nordokod.scio.kt.utils.TaskResult

interface IAuthRepository {
    suspend fun signInWithMail(email: String, password: String): TaskResult<User>
    suspend fun signInWithGoogle(token: String): TaskResult<User>
    suspend fun signInWithFacebook(token: String): TaskResult<User>
    suspend fun signUpWithMail(email: String, password: String): TaskResult<User>
    suspend fun getBasicUserInfo(): TaskResult<User>
    suspend fun isUserLogged(): TaskResult<Boolean>
    suspend fun sendVerificationMail(): TaskResult<Unit>
    suspend fun refreshUser(): TaskResult<User>
    suspend fun logOut(): TaskResult<Unit>
}