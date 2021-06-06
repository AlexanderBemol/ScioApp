package com.nordokod.scio.kt.modules

import android.content.Context
import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.model.repository.*
import com.nordokod.scio.kt.model.source.local.AppDAO
import com.nordokod.scio.kt.model.source.local.GuideDAO
import com.nordokod.scio.kt.model.source.local.QuestionDAO
import com.nordokod.scio.kt.model.source.local.UserDAO
import com.nordokod.scio.kt.model.source.remote.*
import org.koin.dsl.module

val repositoryModule = module {
    fun provideAuthRepository(remoteAuth: IRemoteAuth, localUserDAO: UserDAO) =
            AuthRepository(remoteAuth,localUserDAO) as IAuthRepository
    fun provideUserRepository(remoteUser: IRemoteUser, userDAO: UserDAO, context: Context) =
            UserRepository(remoteUser, userDAO, context) as IUserRepository
    fun provideGuideRepository(remoteGuide: IRemoteGuide, guideDAO: GuideDAO) =
            GuideRepository(remoteGuide,guideDAO) as IGuideRepository
    fun provideQuestionRepository(guide: Guide,remoteQuestion: IRemoteQuestion, questionDAO: QuestionDAO) =
            QuestionRepository(guide,remoteQuestion,questionDAO) as IQuestionRepository
    fun provideAppRepository(appDAO: AppDAO) =
            AppRepository(appDAO) as IAppRepository

    factory { provideAuthRepository(get(),get())}
    factory { provideUserRepository(get(),get(),get()) }
    factory { provideGuideRepository(get(),get()) }
    factory { (guide : Guide) -> provideQuestionRepository(guide,get(),get()) }
    factory { provideAppRepository(get()) }
}