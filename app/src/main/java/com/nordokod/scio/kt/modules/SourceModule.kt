package com.nordokod.scio.kt.modules

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nordokod.scio.kt.model.source.local.AppDatabase
import com.nordokod.scio.kt.model.source.remote.RemoteAuth
import com.nordokod.scio.kt.model.source.remote.RemoteGuide
import com.nordokod.scio.kt.model.source.remote.RemoteQuestion
import com.nordokod.scio.kt.model.source.remote.RemoteUser
import org.koin.dsl.module

val sourceModule = module {
    fun provideRemoteAuth(firebaseAuth: FirebaseAuth) = RemoteAuth(firebaseAuth)
    fun provideRemoteUser(firebaseAuth: FirebaseAuth,firestore: FirebaseFirestore, firebaseStorage: FirebaseStorage) = RemoteUser(firebaseAuth, firestore, firebaseStorage)
    fun provideRemoteGuide(firestore: FirebaseFirestore, firebaseAuth: FirebaseAuth,dynamicLinks: FirebaseDynamicLinks) = RemoteGuide(firestore, firebaseAuth, dynamicLinks)
    fun provideRemoteQuestion(firebaseAuth: FirebaseAuth,firebaseFirestore: FirebaseFirestore) = RemoteQuestion(firebaseFirestore,firebaseAuth)
    fun provideUserDAO(database: AppDatabase) = database.userDAO
    fun provideGuideDAO(database: AppDatabase) = database.guideDAO

    single { provideRemoteAuth(get()) }
    single { provideRemoteUser(get(),get(),get()) }
    single { provideRemoteGuide(get(),get(),get()) }
    single { provideRemoteQuestion(get(),get()) }
    single { provideUserDAO(get()) }
    single { provideGuideDAO(get()) }
}