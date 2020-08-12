package com.nordokod.scio.kt.modules

import com.google.firebase.auth.FirebaseAuth
import com.nordokod.scio.kt.model.source.AppDatabase
import com.nordokod.scio.kt.model.source.UserDAO
import org.koin.dsl.module

val sourceModule = module {
    fun provideAuthSource(firebaseAuth: FirebaseAuth): com.nordokod.scio.kt.model.source.RemoteAuth {
        return com.nordokod.scio.kt.model.source.RemoteAuth(firebaseAuth)
    }

    fun provideUserDAO(database: AppDatabase): UserDAO {
        return database.userDAO
    }

    single { provideAuthSource(get()) }
    single { provideUserDAO(get()) }
}