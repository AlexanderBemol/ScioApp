package com.nordokod.scio.kt.modules

import com.nordokod.scio.kt.model.repository.AuthRepository
import com.nordokod.scio.kt.model.repository.IAuthRepository
import com.nordokod.scio.kt.model.source.RemoteAuth
import org.koin.dsl.module

val repositoryModule = module {
    fun provideAuthRepository(remoteAuth: RemoteAuth): IAuthRepository {
        return AuthRepository(remoteAuth)
    }

    single { provideAuthRepository(get()) }
}