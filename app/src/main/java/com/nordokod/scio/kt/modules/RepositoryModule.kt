package com.nordokod.scio.kt.modules

import android.content.Context
import com.nordokod.scio.kt.model.repository.*
import com.nordokod.scio.kt.model.source.local.GuideDAO
import com.nordokod.scio.kt.model.source.remote.RemoteAuth
import com.nordokod.scio.kt.model.source.remote.RemoteUser
import com.nordokod.scio.kt.model.source.local.UserDAO
import com.nordokod.scio.kt.model.source.remote.RemoteGuide
import org.koin.dsl.module

val repositoryModule = module {
    fun provideAuthRepository(remoteAuth: RemoteAuth) =
            AuthRepository(remoteAuth) as IAuthRepository
    fun provideUserRepository(remoteUser: RemoteUser, userDAO: UserDAO, context: Context) =
            UserRepository(remoteUser, userDAO, context) as IUserRepository
    fun provideGuideRepository(remoteGuide: RemoteGuide, guideDAO: GuideDAO) =
            GuideRepository(remoteGuide,guideDAO) as IGuideRepository

    factory { provideAuthRepository(get())}
    factory { provideUserRepository(get(),get(),get()) }
    factory { provideGuideRepository(get(),get()) }

}