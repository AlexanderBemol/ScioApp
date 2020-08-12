package com.nordokod.scio.kt.model.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import com.nordokod.scio.kt.constants.DataTags
import com.nordokod.scio.kt.constants.Generic
import com.nordokod.scio.kt.constants.UnknownException
import com.nordokod.scio.kt.constants.enums.Provider
import com.nordokod.scio.kt.model.entity.User
import com.nordokod.scio.kt.model.source.RemoteUser
import com.nordokod.scio.kt.model.source.UserDAO
import com.nordokod.scio.kt.utils.NetworkManager
import com.nordokod.scio.kt.utils.TaskResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class UserRepository(private val remoteUser: RemoteUser,
                     private val userDAO: UserDAO,
                     private val context: Context
) : IUserRepository {

    override suspend fun saveUserData(user: User): TaskResult<Unit> {
        return if (NetworkManager.isOnline()) {
            try {
                withTimeout(Generic.TIMEOUT_VALUE) {
                    val result = remoteUser.setUserDocument(user)
                    if (result is TaskResult.Success) {
                        user.synchronized = true
                        saveUserOffline(user)
                    } else {
                        user.synchronized = false
                        saveUserOffline(user)
                    }
                }
            } catch (e: Exception) {
                user.synchronized = false
                saveUserOffline(user)
            }
        } else {
            user.synchronized = false
            saveUserOffline(user)
        }
    }

    override suspend fun getUserData(uid: String): TaskResult<User> {
        return try{
            withTimeout(Generic.TIMEOUT_VALUE) {
                val user = userDAO.getUser(uid)
                if (user.synchronized || !NetworkManager.isOnline())
                    TaskResult.Success(user)
                else
                     remoteUser.getUserDocument(uid)
            }
        } catch (e: Exception) {
            TaskResult.Error(e) //pendiente, si el error es del dao intentar con firebase si tiene internet
        }
    }

    override suspend fun saveUserPhoto(photo: File): TaskResult<Unit> {
          return try{
            withTimeout(Generic.TIMEOUT_VALUE){
                //comprimir foto
                //subir a firebase
                remoteUser.saveUserPhoto(photo)
                //guardar en local

            }
        }catch (e: Exception){
              TaskResult.Error(e)
          }
    }

    override suspend fun getUserPhoto(uid: String): TaskResult<Bitmap> {
        return try{
            val file = File(context.filesDir,DataTags.USER_LOCAL_PHOTO)
            if(file.exists()){
                TaskResult.Success(BitmapFactory.decodeFile(file.path))
            }else{
                withTimeout(Generic.TIMEOUT_VALUE){
                    when(val userResult = remoteUser.getUserDocument(uid)){
                        is TaskResult.Success ->{
                            val user = userResult.data
                            val photoResult = if(user.photoURL.contains("facebook")||user.photoURL.contains("facebook")){
                                remoteUser.getUserExternalPhoto(user)
                            }else{
                                remoteUser.getUserFirebasePhoto(uid)
                            }
                            when(photoResult){
                                is TaskResult.Success ->{
                                    savePhotoInLocal(photoResult.data)
                                    TaskResult.Success(photoResult.data)
                                }
                                is TaskResult.Error ->{
                                    photoResult
                                }
                            }
                        }
                        is TaskResult.Error ->{
                            userResult
                        }
                    }
                }
            }
            //guardar foto descargada en el local
        }catch (e : Exception){
            TaskResult.Error(e)
        }
    }

    private suspend fun saveUserOffline(user: User): TaskResult<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                userDAO.insertUser(user)
                TaskResult.Success(Unit)
            }
        } catch (e: Exception) {
            TaskResult.Error(UnknownException())
        }
    }

    private fun savePhotoInLocal(bitmap: Bitmap){
        val file = File(context.filesDir,DataTags.USER_LOCAL_PHOTO)
        val outputStream = FileOutputStream(file) as OutputStream
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream)
        outputStream.flush()
        outputStream.close()
    }

}