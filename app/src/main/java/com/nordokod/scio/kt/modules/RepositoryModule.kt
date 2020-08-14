package com.nordokod.scio.kt.modules

import android.content.Context
import com.nordokod.scio.kt.model.repository.AuthRepository
import com.nordokod.scio.kt.model.repository.IAuthRepository
import com.nordokod.scio.kt.model.repository.UserRepository
import com.nordokod.scio.kt.model.source.RemoteAuth
import com.nordokod.scio.kt.model.source.RemoteUser
import com.nordokod.scio.kt.model.source.UserDAO
import org.koin.dsl.module

val repositoryModule = module {
    fun provideAuthRepository(remoteAuth: RemoteAuth) = AuthRepository(remoteAuth)
    fun provideUserRepository(remoteUser: RemoteUser,userDAO: UserDAO,context: Context) = UserRepository(remoteUser, userDAO, context)

    single { provideAuthRepository(get())}
    single { provideUserRepository(get(),get(),get()) }
}