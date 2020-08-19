package com.nordokod.scio.kt.model.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.model.entity.User
import com.nordokod.scio.kt.utils.Converters

@Database(
        entities = [User::class,Guide::class],
        version = 1,exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract val userDAO: UserDAO
    abstract val guideDAO: GuideDAO
}