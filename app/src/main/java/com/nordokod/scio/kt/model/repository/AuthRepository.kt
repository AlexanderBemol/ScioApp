package com.nordokod.scio.kt.model.repository

import com.nordokod.scio.kt.model.entity.User
import com.nordokod.scio.kt.model.source.FirebaseAuth
import com.nordokod.scio.kt.utils.TaskResult

class AuthRepository(private val firebaseAuth: FirebaseAuth) : IAuthRepository {
    override suspend fun signInWithMail(email: String, password: String): TaskResult<User> {
        return firebaseAuth.signInWithMail(email, password)
    }

    override suspend fun signInWithGoogle(token: String): TaskResult<User> {
        return firebaseAuth.signInWithGoogle(token)
    }

    override suspend fun signInWithFacebook(token: String): TaskResult<User> {
        return firebaseAuth.signInWithFacebook(token)
    }

    override suspend fun signUpWithMail(email: String, password: String): TaskResult<User> {
        return firebaseAuth.signUpWithMail(email, password)
    }

    override suspend fun getBasicUserInfo(): TaskResult<User> {
        return firebaseAuth.getBasicUserInfo()
    }

    override suspend fun isUserLogged(): TaskResult<Boolean> {
        return firebaseAuth.isUserLogged()
    }

    override suspend fun sendVerificationMail(): TaskResult<Unit> {
        return firebaseAuth.sendVerificationMail()
    }

    override suspend fun refreshUser(): TaskResult<Unit> {
        return firebaseAuth.refreshUser()
    }

    override suspend fun logOut(): TaskResult<Unit> {
        return firebaseAuth.logOut()
    }

}