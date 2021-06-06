package com.nordokod.scio.kt.modules

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nordokod.scio.kt.model.source.local.AppDatabase
import com.nordokod.scio.kt.model.source.remote.*
import org.koin.dsl.module

val sourceModule = module {
    fun provideRemoteAuth(firebaseAuth: FirebaseAuth) = RemoteAuth(firebaseAuth) as IRemoteAuth
    fun provideRemoteUser(firebaseAuth: FirebaseAuth,firestore: FirebaseFirestore, firebaseStorage: FirebaseStorage) = RemoteUser(firebaseAuth, firestore, firebaseStorage) as IRemoteUser
    fun provideRemoteGuide(firestore: FirebaseFirestore, firebaseAuth: FirebaseAuth,dynamicLinks: FirebaseDynamicLinks) = RemoteGuide(firestore, firebaseAuth, dynamicLinks) as IRemoteGuide
    fun provideRemoteQuestion(firebaseAuth: FirebaseAuth,firebaseFirestore: FirebaseFirestore) = RemoteQuestion(firebaseFirestore,firebaseAuth) as IRemoteQuestion
    fun provideUserDAO(database: AppDatabase) = database.userDAO
    fun provideGuideDAO(database: AppDatabase) = database.guideDAO
    fun provideQuestionDao(database: AppDatabase) = database.questionDAO
    fun provideAppDAO(database: AppDatabase) = database.appDAO

    factory { provideRemoteAuth(get()) }
    factory { provideRemoteUser(get(),get(),get()) }
    factory { provideRemoteGuide(get(),get(),get()) }
    factory { provideRemoteQuestion(get(),get()) }

    factory { provideUserDAO(get()) }
    factory { provideGuideDAO(get()) }
    factory { provideQuestionDao(get()) }
    factory { provideAppDAO(get()) }

}