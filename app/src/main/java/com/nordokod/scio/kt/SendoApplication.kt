package com.nordokod.scio.kt

import android.app.Application
import com.nordokod.scio.kt.modules.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SendoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
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