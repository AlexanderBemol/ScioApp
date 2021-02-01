package com.nordokod.scio.source

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.nordokod.scio.TestingValues
import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.model.source.remote.RemoteGuide
import com.nordokod.scio.kt.modules.*
import com.nordokod.scio.kt.utils.TaskResult
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level
import org.koin.test.KoinTest
import org.koin.test.inject

@RunWith(AndroidJUnit4::class)
class GuidesFirebaseTest : KoinTest {
    private val modules = listOf(firebaseModule, sourceModule)
    private val remoteGuide by inject<RemoteGuide>()

    @Before
    fun before(){
        stopKoin()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(InstrumentationRegistry.getInstrumentation().context)
            modules(modules)
        }
    }

    @After
    fun after(){
        stopKoin()
    }

    @Test
    fun createGuide(){
        runBlocking {
            val result = remoteGuide.createGuide(
                    Guide(
                            topic = "TEST",
                            creationUser = "testing-001",
                            updateUser = "testing-001"
                    )
            )
            assertTrue(result is TaskResult.Success)
            Log.d(TestingValues.TESTING_TAG,result.toString())
        }
    }

    @Test
    fun getUserGuidesOnline(){
        runBlocking {
            val result = remoteGuide.getUserGuides()
            assertTrue(result is TaskResult.Success)
            Log.d(TestingValues.TESTING_TAG,result.toString())
        }
    }


}