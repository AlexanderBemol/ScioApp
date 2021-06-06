package com.nordokod.scio.kt.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream
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

    @TypeConverter
    fun bitmapToString(bitmap: Bitmap) : String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream)
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT)
    }

    @TypeConverter
    fun stringToBitmap(string: String) : Bitmap {
        val bytes = Base64.decode(string, Base64.DEFAULT)
        return  BitmapFactory.decodeByteArray(bytes,0,bytes.size)
    }

}