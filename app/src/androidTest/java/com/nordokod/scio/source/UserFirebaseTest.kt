package com.nordokod.scio.source

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.nordokod.scio.TestingValues
import com.nordokod.scio.kt.model.entity.User
import com.nordokod.scio.kt.model.source.remote.RemoteUser
import com.nordokod.scio.kt.modules.firebaseModule
import com.nordokod.scio.kt.modules.sourceModule
import com.nordokod.scio.kt.utils.TaskResult
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject

class UserFirebaseTest : KoinTest {
    private val remoteUser: RemoteUser by inject()
    private val modules = listOf(firebaseModule, sourceModule)

    @Before
    fun before() {
        stopKoin()
        startKoin {
            androidLogger()
            androidContext(InstrumentationRegistry.getInstrumentation().context)
            modules(modules)
        }
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun createUserDocument() {
        val user = User(
                uid = "testing-001",
                displayName = "testing user",
                email = "test@mail.com",
                photoURL = "photo.com",
                synchronized = true
        )
        runBlocking {
            val result = remoteUser.setUserDocument(user)
            assertTrue(result is TaskResult.Success)
        }
    }

    @Test
    fun getUserDocumentOnline() {
        runBlocking {
            val result = remoteUser.getUserDocument("testing-001")
            assertTrue(result is TaskResult.Success)
            if (result is TaskResult.Success) Log.d(TestingValues.TESTING_TAG, result.data.toString())
            else Log.d(TestingValues.TESTING_TAG, result.toString())
        }
    }


}