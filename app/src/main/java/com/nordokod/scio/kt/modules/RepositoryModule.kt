package com.nordokod.scio.kt.modules

import com.nordokod.scio.kt.model.repository.AuthRepository
import com.nordokod.scio.kt.model.repository.IAuthRepository
import com.nordokod.scio.kt.model.source.FirebaseAuth
import org.koin.dsl.module

val repositoryModule = module {
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): IAuthRepository {
        return AuthRepository(firebaseAuth)
    }

    single { provideAuthRepository(get()) }
}