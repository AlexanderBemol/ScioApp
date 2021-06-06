package com.nordokod.scio.kt.model.repository

import com.nordokod.scio.kt.constants.Generic
import com.nordokod.scio.kt.constants.PhoneNetworkException
import com.nordokod.scio.kt.model.entity.User
import com.nordokod.scio.kt.model.source.local.UserDAO
import com.nordokod.scio.kt.model.source.remote.IRemoteAuth
import com.nordokod.scio.kt.model.source.remote.RemoteAuth
import com.nordokod.scio.kt.utils.NetworkManager
import com.nordokod.scio.kt.utils.TaskResult
import com.nordokod.scio.kt.utils.getEnumErrorMessage
import com.nordokod.scio.kt.utils.recordException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

class AuthRepository(
        private val remoteAuth: IRemoteAuth,
        private val localUser: UserDAO
) : IAuthRepository {
    override suspend fun signInWithMail(email: String, password: String): TaskResult<User> {
        return if(NetworkManager.isOnline()){
            try {
                withContext(Dispatchers.IO){
                    withTimeout(Generic.TIMEOUT_VALUE){
                        val result = remoteAuth.signInWithMail(email, password)
                        if(result is TaskResult.Success) insertUserIfNotExist(result.data)
                        result
                    }
                }
            } catch (e : Exception){
                TaskResult.Error(e)
            }
        } else TaskResult.Error(PhoneNetworkException())
    }

    override suspend fun signInWithGoogle(token: String): TaskResult<User> {
        return if(NetworkManager.isOnline()){
            try {
                withContext(Dispatchers.IO){
                    withTimeout(Generic.TIMEOUT_VALUE){
                        val result = remoteAuth.signInWithGoogle(token)
                        if(result is TaskResult.Success) insertUserIfNotExist(result.data)
                        result
                    }
                }
            } catch (e : Exception){
                TaskResult.Error(e)
            }
        } else TaskResult.Error(PhoneNetworkException())
    }

    override suspend fun signInWithFacebook(token: String): TaskResult<User> {
        return if(NetworkManager.isOnline()){
            try {
                withContext(Dispatchers.IO){
                    withTimeout(Generic.TIMEOUT_VALUE){
                        val result = remoteAuth.signInWithFacebook(token)
                        if(result is TaskResult.Success) insertUserIfNotExist(result.data)
                        result
                    }
                }
            } catch (e : Exception){
                TaskResult.Error(e)
            }
        } else TaskResult.Error(PhoneNetworkException())
    }

    override suspend fun signUpWithMail(email: String, password: String): TaskResult<User> {
        return if(NetworkManager.isOnline()){
            try {
                withContext(Dispatchers.IO){
                    withTimeout(Generic.TIMEOUT_VALUE){
                        val result = remoteAuth.signUpWithMail(email, password)
                        if(result is TaskResult.Success) insertUserIfNotExist(result.data)
                        result
                    }
                }
            } catch (e : Exception){
                TaskResult.Error(e)
            }
        } else TaskResult.Error(PhoneNetworkException())
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

    override suspend fun refreshUser(): TaskResult<User> {
        return remoteAuth.refreshUser()
    }

    override suspend fun logOut(): TaskResult<Unit> {
        return remoteAuth.logOut()
    }

    private suspend fun insertUserIfNotExist(user : User){
        try {
            withContext(Dispatchers.IO){
                if(!localUser.existUser(user.uid)){
                    localUser.insertUser(user)
                }
            }
        } catch (e : Exception) {
            if(e.getEnumErrorMessage().getRecordException())
                e.recordException()
        }
    }

}