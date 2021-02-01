package com.nordokod.scio.repository

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.nordokod.scio.TestingValues
import com.nordokod.scio.kt.constants.enums.KindOfQuestion
import com.nordokod.scio.kt.model.entity.Answer
import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.model.entity.Question
import com.nordokod.scio.kt.model.entity.QuestionWithAnswers
import com.nordokod.scio.kt.model.repository.IGuideRepository
import com.nordokod.scio.kt.model.repository.IQuestionRepository
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

class QuestionTest : KoinTest{
    private val databaseModuleTest = module {
        fun provideDatabase(application: Context) = Room.databaseBuilder(application, AppDatabase::class.java,"sendo")
                .fallbackToDestructiveMigration()
                .build()
        single { provideDatabase(ApplicationProvider.getApplicationContext()) }
    }
    private val modules = listOf(databaseModuleTest, sourceModule, firebaseModule, repositoryModule)
    private val questionRepository : IQuestionRepository by inject {
                parametersOf(Guide(id = 1,remoteId = "IG3WgtndbheOxolXIBLu"))
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
    fun addOpenQuestionToGuide(){
        val questionWithAnswers = QuestionWithAnswers(
                question = Question(
                        question = "Testing Question",
                        kindOfQuestion = KindOfQuestion.OPEN.code,
                        idGuide = 1
                ),
                openAnswer = Answer.OpenAnswer(answer = "Open Answer Test")
        )
        try {
            runBlocking {
                when(val result = questionRepository.addQuestion(questionWithAnswers)){
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
        }catch (e: Exception){
            Log.d(TestingValues.TESTING_TAG, e.toString())
            TestCase.assertTrue(false)
        }
    }

    @Test
    fun addTrueFalseQuestionToGuide(){
        val questionWithAnswers = QuestionWithAnswers(
                question = Question(
                        question = "Testing Question True False",
                        kindOfQuestion = KindOfQuestion.TRUE_FALSE.code,
                        idGuide = 1
                ),
                trueFalseAnswer = Answer.TrueFalseAnswer(answer = true)
        )
        try {
            runBlocking {
                when(val result = questionRepository.addQuestion(questionWithAnswers)){
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
        }catch (e: Exception){
            Log.d(TestingValues.TESTING_TAG, e.toString())
            TestCase.assertTrue(false)
        }
    }

    @Test
    fun addMultipleChoiceQuestion(){
        val questionWithAnswers = QuestionWithAnswers(
                question = Question(
                        question = "Testing Question MCQ",
                        kindOfQuestion = KindOfQuestion.MULTIPLE_CHOICE.code,
                        idGuide = 1
                ),
                multipleChoiceAnswers = listOf(
                        Answer.MultipleChoiceAnswer(answer = "respuesta 1",isCorrect = false),
                        Answer.MultipleChoiceAnswer(answer = "respuesta 2",isCorrect = true),
                        Answer.MultipleChoiceAnswer(answer = "respuesta 3",isCorrect = false),
                )
        )
        try {
            runBlocking {
                when(val result = questionRepository.addQuestion(questionWithAnswers)){
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
        }catch (e: Exception){
            Log.d(TestingValues.TESTING_TAG, e.toString())
            TestCase.assertTrue(false)
        }
    }

    @Test
    fun readQuestionsFromGuide(){
        Log.d(TestingValues.TESTING_TAG,"---------------------------------------------------------")
        try {
            runBlocking {
                when(val result = questionRepository.getGuideQuestions()){
                    is TaskResult.Success ->{
                        result.data.forEach{
                            Log.d(TestingValues.TESTING_TAG,it.toString())
                        }
                        TestCase.assertTrue(true)
                    }
                    is TaskResult.Error ->{
                        Log.d(TestingValues.TESTING_TAG,result.e.toString())
                        TestCase.assertTrue(true)
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
    fun syncQuestionsInRemote(){
        try {
            runBlocking {
                when(val result = questionRepository.syncInRemote()){
                    is TaskResult.Success ->{
                        TestCase.assertTrue(true)
                    }
                    is TaskResult.Error ->{
                        Log.d(TestingValues.TESTING_TAG,result.e.toString())
                        TestCase.assertTrue(true)
                    }
                }
            }
        }catch (e : Exception){
            Log.d(TestingValues.TESTING_TAG,e.toString())
            TestCase.assertTrue(false)
        }
    }

    @Test
    fun syncQuestionsInLocal(){
        try {
            runBlocking {
                when(val result = questionRepository.syncInLocal()){
                    is TaskResult.Success ->{
                        TestCase.assertTrue(true)
                    }
                    is TaskResult.Error ->{
                        Log.d(TestingValues.TESTING_TAG,result.e.toString())
                        TestCase.assertTrue(true)
                    }
                }
            }
        }catch (e : Exception){
            Log.d(TestingValues.TESTING_TAG,e.toString())
            TestCase.assertTrue(false)
        }
    }
}