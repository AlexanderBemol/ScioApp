package com.nordokod.scio.repository

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.nordokod.scio.TestingValues
import com.nordokod.scio.kt.model.entity.User
import com.nordokod.scio.kt.model.repository.IUserRepository
import com.nordokod.scio.kt.model.repository.UserRepository
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
import org.koin.core.logger.Level
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

class UserTest : KoinTest {
    private val databaseModuleTest = module {
        fun provideDatabase(application: Context) = Room.databaseBuilder(application, AppDatabase::class.java, "sendo")
                .fallbackToDestructiveMigration()
                .build()
        single { provideDatabase(ApplicationProvider.getApplicationContext<Context>()) }
    }
    private val modules = listOf(databaseModuleTest, sourceModule, firebaseModule, repositoryModule)
    private val userRepository: IUserRepository by inject{
        parametersOf(ApplicationProvider.getApplicationContext())
    }

    @Before
    fun before() {
        stopKoin()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(InstrumentationRegistry.getInstrumentation().context)
            modules(modules)
        }
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun saveUserData() {
        val user = User(
                uid = TestingValues.TEST_USER_UID,
                displayName = "testing user",
                email = "test@mail.com",
                photoURL = "photo.com",
                synchronized = true
        )
        runBlocking {
            try {
                when (val result = userRepository.saveUserData(user)) {
                    is TaskResult.Success -> TestCase.assertTrue(true)
                    is TaskResult.Error -> {
                        Log.d(TestingValues.TESTING_TAG, result.e.toString())
                        TestCase.assertTrue(false)
                    }
                }
            } catch (e: Exception) {
                Log.d(TestingValues.TESTING_TAG, e.toString())
                TestCase.assertTrue(false)
            }
        }
    }

    @Test
    fun getUserData() {
        runBlocking {
            try {
                when (val result = userRepository.getUserData(TestingValues.TEST_USER_UID)) {
                    is TaskResult.Success -> {
                        Log.d(TestingValues.TESTING_TAG, result.data.toString())
                        TestCase.assertTrue(true)
                    }
                    is TaskResult.Error -> {
                        Log.d(TestingValues.TESTING_TAG, result.e.toString())
                        TestCase.assertTrue(false)
                    }
                }
            } catch (e: Exception) {
                Log.d(TestingValues.TESTING_TAG, e.toString())
                TestCase.assertTrue(false)
            }
        }
    }

}