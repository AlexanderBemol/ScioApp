package com.nordokod.scio.kt.model.entity

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class AppPackage(
        @PrimaryKey var packagePath : String = "",
        var name : String = "",
        @Ignore var icon : Bitmap = Bitmap.createBitmap(10,10,Bitmap.Config.ARGB_8888),
        @Ignore var locked : Boolean = false
)