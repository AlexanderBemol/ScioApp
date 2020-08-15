package com.nordokod.scio.source

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.nordokod.scio.kt.constants.Testing
import com.nordokod.scio.kt.model.entity.User
import com.nordokod.scio.kt.model.source.AppDatabase
import com.nordokod.scio.kt.model.source.UserDAO
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

class UserLocalTest : KoinTest {
    private val databaseModuleTest = module {
        fun provideDatabase(application: Context) = Room.databaseBuilder(application, AppDatabase::class.java, "sendo")
                .fallbackToDestructiveMigration()
                .build()
        single { provideDatabase(ApplicationProvider.getApplicationContext<Context>()) }
    }
    private val modules = listOf(databaseModuleTest, sourceModule)
    private val localUser: UserDAO by inject()

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
    fun saveUser(){
        val user = User(
                uid = "testing-001",
                displayName = "testing user",
                email = "test@mail.com",
                photoURL = "photo.com",
                synchronized = true
        )
        runBlocking {
            try{
                localUser.insertUser(user)
                TestCase.assertTrue(true)
            } catch (e: Exception){
                Log.d(Testing.TESTING_TAG,e.toString())
                TestCase.assertTrue(false)
            }
        }
    }

    @Test
    fun getUser(){
        runBlocking {
            try{
                val user = localUser.getUser("testing-001")
                Log.d(Testing.TESTING_TAG,user.toString())
                TestCase.assertTrue(true)
            } catch (e: Exception){
                Log.d(Testing.TESTING_TAG,e.toString())
                TestCase.assertTrue(false)
            }
        }
    }


}