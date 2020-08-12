package com.nordokod.scio.kt.modules

import android.app.Application
import androidx.room.Room
import com.nordokod.scio.kt.model.source.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    fun provideDatabase(application: Application): AppDatabase{
        return Room.databaseBuilder(application,AppDatabase::class.java,"sendo")
                .fallbackToDestructiveMigration()
                .build()
    }

    single { provideDatabase(androidApplication()) }
}