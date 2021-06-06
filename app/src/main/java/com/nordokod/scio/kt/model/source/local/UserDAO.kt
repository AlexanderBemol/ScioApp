package com.nordokod.scio.kt.model.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nordokod.scio.kt.model.entity.User

@Dao
interface UserDAO {
    @Query("SELECT * FROM User where uid = :uid")
    suspend fun getUser(uid: String): User
     @Query("SELECT EXISTS(SELECT * FROM User WHERE uid = :uid)")
     suspend fun existUser(uid: String) : Boolean
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)
}