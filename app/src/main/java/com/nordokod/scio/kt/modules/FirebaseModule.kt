package com.nordokod.scio.kt.modules

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.dsl.module

val firebaseModule = module {
    fun provideFirebaseAuth()  = FirebaseAuth.getInstance()
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()
    fun provideFirebaseStorage() = FirebaseStorage.getInstance()
    fun provideFirebaseDynamicLinks() = FirebaseDynamicLinks.getInstance()

    single { provideFirebaseAuth() }
    single { provideFirebaseFirestore() }
    single { provideFirebaseStorage() }
    single { provideFirebaseDynamicLinks() }
}