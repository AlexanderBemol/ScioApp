package com.nordokod.scio.kt.modules

import android.content.Context
import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.model.repository.*
import com.nordokod.scio.kt.model.source.local.GuideDAO
import com.nordokod.scio.kt.model.source.local.QuestionDAO
import com.nordokod.scio.kt.model.source.remote.RemoteAuth
import com.nordokod.scio.kt.model.source.remote.RemoteUser
import com.nordokod.scio.kt.model.source.local.UserDAO
import com.nordokod.scio.kt.model.source.remote.RemoteGuide
import com.nordokod.scio.kt.model.source.remote.RemoteQuestion
import org.koin.dsl.module

val repositoryModule = module {
    fun provideAuthRepository(remoteAuth: RemoteAuth) =
            AuthRepository(remoteAuth) as IAuthRepository
    fun provideUserRepository(remoteUser: RemoteUser, userDAO: UserDAO, context: Context) =
            UserRepository(remoteUser, userDAO, context) as IUserRepository
    fun provideGuideRepository(remoteGuide: RemoteGuide, guideDAO: GuideDAO) =
            GuideRepository(remoteGuide,guideDAO) as IGuideRepository
    fun provideQuestionRepository(guide: Guide,remoteQuestion: RemoteQuestion, questionDAO: QuestionDAO) =
            QuestionRepository(guide,remoteQuestion,questionDAO) as IQuestionRepository

    factory { provideAuthRepository(get())}
    factory { provideUserRepository(get(),get(),get()) }
    factory { provideGuideRepository(get(),get()) }
    factory { (guide : Guide) -> provideQuestionRepository(guide,get(),get()) }
}