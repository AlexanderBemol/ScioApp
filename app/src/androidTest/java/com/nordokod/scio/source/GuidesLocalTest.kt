package com.nordokod.scio.source

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.nordokod.scio.TestingValues
import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.model.source.local.AppDatabase
import com.nordokod.scio.kt.model.source.local.GuideDAO
import com.nordokod.scio.kt.modules.sourceModule
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

class GuidesLocalTest : KoinTest {
    private val databaseModuleTest = module {
        fun provideDatabase(application: Context) = Room.databaseBuilder(application, AppDatabase::class.java, "sendo")
                .fallbackToDestructiveMigration()
                .build()
        single { provideDatabase(ApplicationProvider.getApplicationContext<Context>()) }
    }
    private val modules = listOf(databaseModuleTest, sourceModule)
    private val localGuide: GuideDAO by inject()

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
    fun saveLocalGuide() {
        runBlocking {
            try {
                localGuide.insertGuide(Guide(
                        creationUser = "testing-001",
                        updateUser = "testing-001",
                        topic = "TEST"
                ))
                TestCase.assertTrue(true)
            } catch (e: Exception) {
                Log.d(TestingValues.TESTING_TAG, e.toString())
                TestCase.assertTrue(false)
            }
        }
    }

    @Test
    fun getLocalGuides() {
        runBlocking {
            try {
                val userWithGuides = localGuide.getGuidesFromUser(TestingValues.TEST_USER_UID)
                Log.d(TestingValues.TESTING_TAG, userWithGuides.toString())
                TestCase.assertTrue(true)
            } catch (e: Exception) {
                Log.d(TestingValues.TESTING_TAG, e.toString())
                TestCase.assertTrue(false)
            }
        }
    }

    @Test
    fun deleteAllGuides() {
        runBlocking {
            try {
                localGuide.deleteAllGuides()
                TestCase.assertTrue(true)
            } catch (e: Exception) {
                Log.d(TestingValues.TESTING_TAG, e.toString())
                TestCase.assertTrue(false)
            }
        }
    }
}