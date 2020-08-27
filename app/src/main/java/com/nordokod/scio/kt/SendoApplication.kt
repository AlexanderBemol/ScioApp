package com.nordokod.scio.kt

import android.app.Application
import com.nordokod.scio.kt.modules.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class SendoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE) //temporal by bug in kt1.4
            androidContext(this@SendoApplication)
            modules(
                    firebaseModule,
                    databaseModule,
                    sourceModule,
                    repositoryModule,
                    viewModelModule
            )
        }


    }
}