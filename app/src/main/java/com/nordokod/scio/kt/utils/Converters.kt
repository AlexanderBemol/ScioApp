package com.nordokod.scio.kt.utils

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun dateFromTimestamp(timestamp: Long) : Date {
        return Date(timestamp)
    }
    @TypeConverter
    fun dateToTimestamp(date : Date) : Long {
        return date.time
    }
}