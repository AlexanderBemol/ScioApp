package com.nordokod.scio.source

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.nordokod.scio.TestingValues
import com.nordokod.scio.kt.model.entity.User
import com.nordokod.scio.kt.model.source.local.AppDatabase
import com.nordokod.scio.kt.model.source.local.UserDAO
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
import org.koin.core.logger.Level
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

class UserLocalTest : KoinTest {
    private val databaseModuleTest = module {
        fun provideDatabase(application: Context) = Room.databaseBuilder(application, AppDatabase::class.java, "sendo")
                .fallbackToDestructiveMigration()
                .build()
        single { provideDatabase(ApplicationProvider.getApplicationContext()) }
    }
    private val modules = listOf(databaseModuleTest, sourceModule)
    private val localUser: UserDAO by inject()

    @Before
    fun before() {
        stopKoin()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(InstrumentationRegistry.getInstrumentation().context)
            modules(modules)
        }
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun saveUser(){
        val user = User(
                uid = TestingValues.TEST_USER_UID,
                displayName = TestingValues.TEST_USERNAME,
                email = TestingValues.TEST_MAIL,
                photoURL = "photo.com",
                synchronized = true
        )
        runBlocking {
            try{
                localUser.insertUser(user)
                TestCase.assertTrue(true)
            } catch (e: Exception){
                Log.d(TestingValues.TESTING_TAG,e.toString())
                TestCase.assertTrue(false)
            }
        }
    }

    @Test
    fun getUser(){
        runBlocking {
            try{
                saveUser()
                val user = localUser.getUser(TestingValues.TEST_USER_UID)
                Log.d(TestingValues.TESTING_TAG,user.toString())
                TestCase.assertTrue(true)
            } catch (e: Exception){
                Log.d(TestingValues.TESTING_TAG,e.toString())
                TestCase.assertTrue(false)
            }
        }
    }


}