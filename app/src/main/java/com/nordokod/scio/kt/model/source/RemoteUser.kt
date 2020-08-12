package com.nordokod.scio.kt.model.source

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.nordokod.scio.kt.constants.DataTags
import com.nordokod.scio.kt.constants.UnknownException
import com.nordokod.scio.kt.model.entity.User
import com.nordokod.scio.kt.utils.TaskResult
import kotlinx.coroutines.tasks.await
import java.io.File
import com.nordokod.scio.kt.utils.getEnumErrorMessage;
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class RemoteUser(
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
            firebaseDB.collection(DataTags.USERS_COLLECTION).document(user.uid).set(data).await()
            TaskResult.Success(Unit)
        } catch (e: Exception) {
            print(e.message)
            TaskResult.Error(e)
        }
    }

    /**
     * Get an user document from firestore database
     * @param uid: uid from the user to retrieve
     * @param source: Source Firebase source
     * @return TaskResult<User>
     */
    suspend fun getUserDocument(uid: String): TaskResult<User> {
        return try {
            val result = firebaseDB.collection(DataTags.USERS_COLLECTION).document(uid).get().await()
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
                            creationDate = result.getDate(User::creationDate.name) ?: Date(),
                            synchronized = true
                    )
            )
        } catch (e: Exception) {
            e.getEnumErrorMessage()
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
                val result = firebaseStorage.reference.child(DataTags.USERS_FOLDER)
                        .child(DataTags.USERS_PROFILE_PHOTO)
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
     * @return TaskResult<Bitmap>
     */
    suspend fun getUserFirebasePhoto(uid: String): TaskResult<Bitmap> {
        return try {
            val result = firebaseStorage.reference
                    .child(DataTags.USERS_FOLDER)
                    .child(DataTags.USERS_PROFILE_PHOTO)
                    .getBytes(1024)
                    .await()
            TaskResult.Success(BitmapFactory.decodeByteArray(result,0,result.size))
        } catch (e: Exception) {
            TaskResult.Error(e)
        }
    }

    /**
     * get the user profile photo from facebook or google
     * @param user
     * @return TaskResult<Bitmap>
     */
    fun getUserExternalPhoto(user: User): TaskResult<Bitmap>{
        var photoPath = user.photoURL
        if(photoPath.contains(FacebookAuthProvider.PROVIDER_ID)) photoPath += "?type=large"
        return try{
            val url = URL(photoPath)
            val connection =  url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val bitmap = BitmapFactory.decodeStream(connection.inputStream)
            TaskResult.Success(bitmap)
        }catch (e: Exception){
            TaskResult.Error(e)
        }
    }

}