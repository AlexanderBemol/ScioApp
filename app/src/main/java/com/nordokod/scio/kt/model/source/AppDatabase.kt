package com.nordokod.scio.kt.model.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.nordokod.scio.kt.model.entity.User
import com.nordokod.scio.kt.utils.Converters

@Database(
        entities = [User::class],
        version = 1,exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract val userDAO: UserDAO
}