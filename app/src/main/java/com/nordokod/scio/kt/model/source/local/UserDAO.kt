package com.nordokod.scio.kt.model.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nordokod.scio.kt.model.entity.User

@Dao
interface UserDAO {
    @Query("SELECT * FROM User where uid = :uid")
    fun getUser(uid: String): User
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)
}