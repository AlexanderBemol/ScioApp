package com.nordokod.scio.kt.model.repository

import com.nordokod.scio.kt.model.entity.User
import com.nordokod.scio.kt.model.source.RemoteAuth
import com.nordokod.scio.kt.utils.TaskResult

class AuthRepository(private val remoteAuth: RemoteAuth) : IAuthRepository {
    override suspend fun signInWithMail(email: String, password: String): TaskResult<User> {
        return remoteAuth.signInWithMail(email, password)
    }

    override suspend fun signInWithGoogle(token: String): TaskResult<User> {
        return remoteAuth.signInWithGoogle(token)
    }

    override suspend fun signInWithFacebook(token: String): TaskResult<User> {
        return remoteAuth.signInWithFacebook(token)
    }

    override suspend fun signUpWithMail(email: String, password: String): TaskResult<User> {
        return remoteAuth.signUpWithMail(email, password)
    }

    override suspend fun getBasicUserInfo(): TaskResult<User> {
        return remoteAuth.getBasicUserInfo()
    }

    override suspend fun isUserLogged(): TaskResult<Boolean> {
        return remoteAuth.isUserLogged()
    }

    override suspend fun sendVerificationMail(): TaskResult<Unit> {
        return remoteAuth.sendVerificationMail()
    }

    override suspend fun refreshUser(): TaskResult<Unit> {
        return remoteAuth.refreshUser()
    }

    override suspend fun logOut(): TaskResult<Unit> {
        return remoteAuth.logOut()
    }

}