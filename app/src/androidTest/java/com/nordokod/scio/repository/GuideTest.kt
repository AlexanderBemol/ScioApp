package com.nordokod.scio.repository

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.nordokod.scio.TestingValues
import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.model.repository.GuideRepository
import com.nordokod.scio.kt.model.source.local.AppDatabase
import com.nordokod.scio.kt.modules.firebaseModule
import com.nordokod.scio.kt.modules.repositoryModule
import com.nordokod.scio.kt.modules.sourceModule
import com.nordokod.scio.kt.utils.TaskResult
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

class GuideTest : KoinTest {
    private val databaseModuleTest = module {
        fun provideDatabase(application: Context) = Room.databaseBuilder(application, AppDatabase::class.java, "sendo")
                .fallbackToDestructiveMigration()
                .build()
        single { provideDatabase(ApplicationProvider.getApplicationContext<Context>()) }
    }
    private val modules = listOf(databaseModuleTest, sourceModule, firebaseModule, repositoryModule)
    private val guideRepository: GuideRepository by inject()

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
    fun createGuide() {
        val guide = Guide(
                topic = "TESTING",
                creationUser = TestingValues.TEST_USER_UID,
                updateUser = TestingValues.TEST_USER_UID
        )
        try {
            runBlocking {
                when(val result = guideRepository.createGuide(guide)){
                    is TaskResult.Success -> {
                        Log.d(TestingValues.TESTING_TAG,result.data.toString())
                        TestCase.assertTrue(true)
                    }
                    is TaskResult.Error -> {
                        Log.d(TestingValues.TESTING_TAG,result.e.toString())
                        TestCase.assertTrue(true)
                    }
                }
            }
        } catch (e: Exception) {
            Log.d(TestingValues.TESTING_TAG, e.toString())
            TestCase.assertTrue(false)
        }
    }

    @Test
    fun updateGuide() {
        val guide = Guide(
                id = 9,
                topic = "NEW TESTING",
                creationUser = TestingValues.TEST_USER_UID,
                updateUser = TestingValues.TEST_USER_UID
        )
        try {
            runBlocking {
                when(val result = guideRepository.updateGuide(guide)){
                    is TaskResult.Success -> {
                        TestCase.assertTrue(true)
                    }
                    is TaskResult.Error -> {
                        Log.d(TestingValues.TESTING_TAG,result.e.toString())
                        TestCase.assertTrue(true)
                    }
                }
            }
        } catch (e: Exception) {
            Log.d(TestingValues.TESTING_TAG, e.toString())
            TestCase.assertTrue(false)
        }
    }

    @Test
    fun readGuides(){
        try{
            Log.d(TestingValues.TESTING_TAG,"--------------------READ GUIDES------------------")
            runBlocking {
                when(val result = guideRepository.getUserGuides(TestingValues.TEST_USER_UID)){
                    is TaskResult.Success ->{
                        result.data.forEach{
                            Log.d(TestingValues.TESTING_TAG,it.toString())
                        }
                        TestCase.assertTrue(true)
                    }
                    is TaskResult.Error->{
                        Log.d(TestingValues.TESTING_TAG,result.e.toString())
                        TestCase.assertTrue(false)
                    }
                }
            }
        }catch (e : Exception){
            Log.d(TestingValues.TESTING_TAG,e.toString())
            TestCase.assertTrue(false)
        }
        Log.d(TestingValues.TESTING_TAG,"---------------------------------------------------------")
    }

    @Test
    fun deleteGuide(){
        try{
            runBlocking {
                when (val result = guideRepository.deleteGuide(Guide(id = 14))) {
                    is TaskResult.Success -> {
                        TestCase.assertTrue(true)
                    }
                    is TaskResult.Error -> {
                        Log.d(TestingValues.TESTING_TAG, result.e.toString())
                        TestCase.assertTrue(false)
                    }
                }
            }
        }catch (e : Exception){
            Log.d(TestingValues.TESTING_TAG,e.toString())
            TestCase.assertTrue(false)
        }
    }

    @Test
    fun syncInRemote(){
        try{
            runBlocking {
                when (val result = guideRepository.syncInRemote(TestingValues.TEST_USER_UID)) {
                    is TaskResult.Success -> {
                        TestCase.assertTrue(true)
                    }
                    is TaskResult.Error -> {
                        Log.d(TestingValues.TESTING_TAG, result.e.toString())
                        TestCase.assertTrue(false)
                    }
                }
            }
        }catch (e : Exception){
            Log.d(TestingValues.TESTING_TAG,e.toString())
            TestCase.assertTrue(false)
        }
    }

}