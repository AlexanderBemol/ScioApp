package com.nordokod.scio.kt.model.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nordokod.scio.kt.model.entity.Answer
import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.model.entity.Question
import com.nordokod.scio.kt.model.entity.User
import com.nordokod.scio.kt.utils.Converters

@Database(
        entities = [
            User::class,
            Guide::class,
            Question::class,
            Answer.MultipleChoiceAnswer::class,
            Answer.TrueFalseAnswer::class,
            Answer.OpenAnswer::class
        ],
        version = 2,
        exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDAO: UserDAO
    abstract val guideDAO: GuideDAO
    abstract val questionDAO: QuestionDAO
}