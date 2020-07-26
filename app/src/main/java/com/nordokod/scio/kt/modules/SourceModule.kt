package com.nordokod.scio.kt.modules

import com.google.firebase.auth.FirebaseAuth
import org.koin.dsl.module

val sourceModule = module {
    fun provideAuthSource(firebaseAuth: FirebaseAuth): com.nordokod.scio.kt.model.source.FirebaseAuth{
        return com.nordokod.scio.kt.model.source.FirebaseAuth(firebaseAuth)
    }

    single { provideAuthSource(get())}
}