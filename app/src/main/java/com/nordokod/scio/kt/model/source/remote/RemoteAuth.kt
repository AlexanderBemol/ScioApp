package com.nordokod.scio.kt.model.source.remote

import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.nordokod.scio.kt.model.entity.User
import com.nordokod.scio.kt.utils.TaskResult
import com.nordokod.scio.kt.constants.enums.Provider
import com.nordokod.scio.kt.constants.enums.UserState
import kotlinx.coroutines.tasks.await
import java.util.*

class RemoteAuth(private val firebaseAuth: FirebaseAuth) : IRemoteAuth {

    /**
     * Sign in with mail
     * @param email: String with the email
     * @param password: String with user password
     * @return FBResult<User> Object with the task result
     */
    override suspend fun signInWithMail(email: String, password: String): TaskResult<User> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            TaskResult.Success(
                    User(
                            uid = result.user?.uid ?: "",
                            emailVerified = result.user?.isEmailVerified ?: false,
                            newUser = result.user?.isEmailVerified ?: false,
                            displayName = result.user?.displayName ?: "",
                            email = result.user?.email ?: "",
                            provider = Provider.MAIL.code,
                            synchronized = true
                    )
            )
        } catch (e: Exception) {
            TaskResult.Error(e)
        }
    }

    /**
     * Create a new user with the password and email provided
     * @param email: String with the email
     * @param password: String with user password
     * @return FBResult<User> Object with the task result
     */
    override suspend fun signUpWithMail(email: String, password: String): TaskResult<User> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.sendEmailVerification()
            TaskResult.Success(
                    User(
                            uid = result.user?.uid ?: "",
                            newUser = true,
                            email = result.user?.email ?: "",
                            synchronized = true
                    )
            )
        } catch (e: Exception) {
            TaskResult.Error(e)
        }
    }

    /**
     * Sign in with Google
     * @param token: String with the auth token
     * @return FBResult<User> Object with the task result
     */
    override suspend fun signInWithGoogle(token: String): TaskResult<User> {
        return try {
            val result = firebaseAuth.signInWithCredential(GoogleAuthProvider.getCredential(token, null)).await()
            TaskResult.Success(
                    User(
                            uid = result.user?.uid ?: "",
                            emailVerified = true,
                            newUser = result.additionalUserInfo?.isNewUser ?: true,
                            displayName = result.user?.displayName ?: "",
                            email = result.user?.email ?: "",
                            photoURL = result.user?.photoUrl.toString(),
                            provider = Provider.GOOGLE.code,
                            synchronized = true
                    )
            )
        } catch (e: Exception) {
            TaskResult.Error(e)
        }
    }

    /**
     * Sign in with Facebook
     * @param token: String with the auth token
     * @return FBResult<User> Object with the task result
     */
    override suspend fun signInWithFacebook(token: String): TaskResult<User> {
        return try {
            val result = firebaseAuth.signInWithCredential(FacebookAuthProvider.getCredential(token)).await()
            TaskResult.Success(
                    User(
                            uid = result.user?.uid ?: "",
                            emailVerified = true,
                            newUser = result.additionalUserInfo?.isNewUser ?: true,
                            displayName = result.user?.displayName ?: "",
                            email = result.user?.email ?: "",
                            photoURL = result.user?.photoUrl.toString(),
                            provider = Provider.FACEBOOK.code,
                            userState = UserState.FREE.code,
                            creationDate = Date(),
                            synchronized = true
                    )
            )
        } catch (e: Exception) {
            TaskResult.Error(e)
        }
    }

    /**
     * get all the basic data of a user
     * @return FBResult<User> Object with the result
     */
    override fun getBasicUserInfo(): TaskResult<User> = TaskResult.Success(
            firebaseAuth.currentUser?.let {
                User(
                        uid = it.uid,
                        emailVerified = it.isEmailVerified,
                        displayName = it.displayName.toString(),
                        email = it.email.toString(),
                        photoURL = it.photoUrl.toString(),
                        provider = when (it.providerData[0].providerId) {
                            GoogleAuthProvider.PROVIDER_ID -> Provider.GOOGLE.code
                            FacebookAuthProvider.PROVIDER_ID -> Provider.FACEBOOK.code
                            else -> Provider.MAIL.code
                        },
                        synchronized = true
                )
            } ?: User()
    )

    /**
     * Is the user logged?
     * @return FBResult<Boolean> with task result
     */
    override fun isUserLogged(): TaskResult<Boolean> = TaskResult.Success(firebaseAuth.currentUser != null)

    /**
     * Log out
     * @return FBResult<Unit> with the task result
     */
    override fun logOut(): TaskResult<Unit> = TaskResult.Success(firebaseAuth.signOut())

    /**
     * Send verification mail
     * @return TaskResult<Unit> with the result
     */
    override suspend fun sendVerificationMail(): TaskResult<Unit> {
        return try {
            firebaseAuth.currentUser?.sendEmailVerification()?.await()
            TaskResult.Success(Unit)
        } catch (e: Exception) {
            TaskResult.Error(e)
        }
    }

    /**
     * Refresh the user data
     * @return TaskResult<Unit> with the result
     */
    override suspend fun refreshUser(): TaskResult<User> {
        return try {
            firebaseAuth.currentUser?.reload()?.await()
            TaskResult.Success( firebaseAuth.currentUser?.let {
                User(
                        uid = it.uid,
                        emailVerified = it.isEmailVerified,
                        displayName = it.displayName.toString(),
                        email = it.email.toString(),
                        photoURL = it.photoUrl.toString(),
                        provider = when (it.providerData[0].providerId) {
                            GoogleAuthProvider.PROVIDER_ID -> Provider.GOOGLE.code
                            FacebookAuthProvider.PROVIDER_ID -> Provider.FACEBOOK.code
                            else -> Provider.MAIL.code
                        },
                        synchronized = true
                )
            } ?: User())
        } catch (e: Exception) {
            TaskResult.Error(e)
        }
    }

}