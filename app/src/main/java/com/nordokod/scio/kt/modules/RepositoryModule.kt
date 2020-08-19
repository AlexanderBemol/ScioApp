package com.nordokod.scio.kt.modules

import android.content.Context
import com.nordokod.scio.kt.model.repository.AuthRepository
import com.nordokod.scio.kt.model.repository.GuideRepository
import com.nordokod.scio.kt.model.repository.UserRepository
import com.nordokod.scio.kt.model.source.local.GuideDAO
import com.nordokod.scio.kt.model.source.remote.RemoteAuth
import com.nordokod.scio.kt.model.source.remote.RemoteUser
import com.nordokod.scio.kt.model.source.local.UserDAO
import com.nordokod.scio.kt.model.source.remote.RemoteGuide
import org.koin.dsl.module

val repositoryModule = module {
    fun provideAuthRepository(remoteAuth: RemoteAuth) = AuthRepository(remoteAuth)
    fun provideUserRepository(remoteUser: RemoteUser, userDAO: UserDAO, context: Context) = UserRepository(remoteUser, userDAO, context)
    fun provideGuideRepository(remoteGuide: RemoteGuide, guideDAO: GuideDAO) = GuideRepository(remoteGuide,guideDAO)

    single { provideAuthRepository(get())}
    single { provideUserRepository(get(),get(),get()) }
    single { provideGuideRepository(get(),get()) }

}