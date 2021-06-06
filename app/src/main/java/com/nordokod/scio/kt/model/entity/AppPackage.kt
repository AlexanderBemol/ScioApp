package com.nordokod.scio.kt.model.entity

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AppPackage(
        @PrimaryKey var packagePath : String = "",
        var name : String = "",
        var icon : Bitmap,
        var locked : Boolean = false
)