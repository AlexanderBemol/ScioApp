package com.nordokod.scio.kt

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import com.nordokod.scio.kt.modules.firebaseModule
import com.nordokod.scio.kt.modules.repositoryModule
import com.nordokod.scio.kt.modules.sourceModule
import com.nordokod.scio.kt.modules.viewModelModule

class SendoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger()
            androidContext(this@SendoApplication)
            modules(
                    firebaseModule,
                    sourceModule,
                    repositoryModule,
                    viewModelModule
            )
        }


    }
}