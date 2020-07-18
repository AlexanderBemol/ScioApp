package com.nordokod.scio.source

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nordokod.scio.kt.model.entity.User
import com.nordokod.scio.kt.model.source.FirebaseUser
import com.nordokod.scio.kt.utils.TaskResult
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.*

class UserFirebaseTest {
    private val firebaseDatabase = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()
    private val firebaseUser = FirebaseUser(firebaseAuth, firebaseDatabase, firebaseStorage)

    @Test
    fun testCreateUserDocument() {
        val user = User(
                uid = "testing-001",
                emailVerified = false,
                newUser = false,
                displayName = "testing user",
                email = "test@mail.com",
                photoURL = "photo.com",
                provider = 1,
                userState = 1,
                creationDate = Date()
        )
        runBlocking {
            val result = firebaseUser.setUserDocument(user)
            assertTrue(result is TaskResult.Success)
        }
    }

    @Test
    fun testGetUserDocument(){
        runBlocking {
            val result = firebaseUser.getUserDocument("testing-001")
            assertTrue(result is TaskResult.Success)
            if(result is TaskResult.Success) Log.d("SENDO_TESTING",result.data.toString())
        }
    }

}