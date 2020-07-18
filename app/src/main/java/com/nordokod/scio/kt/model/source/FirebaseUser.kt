package com.nordokod.scio.kt.model.source

import android.net.Uri
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.nordokod.scio.kt.constants.FirebaseTags
import com.nordokod.scio.kt.constants.exceptions.UnknownException
import com.nordokod.scio.kt.model.entity.User
import com.nordokod.scio.kt.utils.TaskResult
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.*

class FirebaseUser(
        private val firebaseAuth: FirebaseAuth,
        private val firebaseDB: FirebaseFirestore,
        private val firebaseStorage: FirebaseStorage
) {

    /**
     * Set an user document in the firestore database
     * @param user User entity with the data
     *@return TaskResult<Unit> with the result
     */
    suspend fun setUserDocument(user: User): TaskResult<Unit> {
        val data = hashMapOf(
                User::displayName.name to user.displayName,
                User::email.name to user.email,
                User::photoURL.name to user.photoURL,
                User::newUser.name to user.newUser,
                User::emailVerified.name to true,
                User::provider.name to user.provider,
                User::userState.name to user.userState,
                User::creationDate.name to FieldValue.serverTimestamp()
        )
        return try {
            firebaseDB.collection(FirebaseTags.USERS_COLLECTION).document(user.uid).set(data).await()
            TaskResult.Success(Unit)
        } catch (e: Exception) {
            print(e.message)
            TaskResult.Error(e)
        }
    }

    /**
     * Get an user document from firestore database
     * @param uid: uid from the user to retrieve
     * @return TaskResult<User>
     */
    suspend fun getUserDocument(uid: String): TaskResult<User> {
        return try {
            val result = firebaseDB.collection(FirebaseTags.USERS_COLLECTION).document(uid).get().await()
            TaskResult.Success(
                    User(
                            uid = result.id,
                            emailVerified = result[User::emailVerified.name] as Boolean,
                            newUser = result[User::newUser.name] as Boolean,
                            displayName = result[User::displayName.name] as String,
                            email = result[User::email.name] as String,
                            photoURL = result[User::photoURL.name] as String,
                            provider = (result[User::provider.name] as Long).toInt(),
                            userState = (result[User::userState.name] as Long).toInt(),
                            creationDate = result.getDate(User::creationDate.name) ?: Date()
                    )
            )
        } catch (e: Exception) {
            TaskResult.Error(e)
        }
    }

    /**
     * upload the user profile photo
     * @param photo: File file of photo
     *@return TaskResult<Unit>
     */
    suspend fun saveUserPhoto(photo: File): TaskResult<Unit> {
        return try {
            if (firebaseAuth.currentUser != null) {
                val filename = firebaseAuth.currentUser!!.uid
                val result = firebaseStorage.reference.child(FirebaseTags.USERS_FOLDER)
                        .child(FirebaseTags.USERS_PROFILE_PHOTO)
                        .child(filename)
                        .putFile(Uri.fromFile(photo))
                        .await()
                TaskResult.Success(Unit)
            } else {
                TaskResult.Error(UnknownException())
            }
        } catch (e: Exception) {
            TaskResult.Error(e)
        }
    }

    /**
     * get the user profile photo from firebase
     * @param uid
     */
    suspend fun getUserFirebasePhoto(uid: String): TaskResult<ByteArray> {
        return try {
            val result = firebaseStorage.reference
                    .child(FirebaseTags.USERS_FOLDER)
                    .child(FirebaseTags.USERS_PROFILE_PHOTO)
                    .getBytes(1024)
                    .await()
            TaskResult.Success(result)
        } catch (e: Exception) {
            TaskResult.Error(e)
        }
    }

}