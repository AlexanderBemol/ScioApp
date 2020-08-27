package com.nordokod.scio.source

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.nordokod.scio.TestingValues
import com.nordokod.scio.kt.constants.enums.KindOfQuestion
import com.nordokod.scio.kt.model.entity.Answer
import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.model.entity.Question
import com.nordokod.scio.kt.model.entity.QuestionWithAnswers
import com.nordokod.scio.kt.model.source.remote.RemoteQuestion
import com.nordokod.scio.kt.modules.firebaseModule
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
import org.koin.test.KoinTest
import org.koin.test.inject

class QuestionFirebaseTest : KoinTest {
    private val modules = listOf(firebaseModule, sourceModule)
    private val remoteQuestion by inject<RemoteQuestion>()

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
    fun createQuestion() {
        val guide = Guide(remoteId = "0RxKmLPY0ovu1WzzQi0J")
        val questionWithAnswers = QuestionWithAnswers(
                question = Question(question = "test-question", kindOfQuestion = KindOfQuestion.MULTIPLE_CHOICE.code),
                openAnswer = Answer.OpenAnswer(answer = "test-answer"),
                trueFalseAnswer = Answer.TrueFalseAnswer(),
                multipleChoiceAnswers = listOf(
                        Answer.MultipleChoiceAnswer(answer = "true", isCorrect = true),
                        Answer.MultipleChoiceAnswer(answer = "false", isCorrect = false),
                        Answer.MultipleChoiceAnswer(answer = "false", isCorrect = false)
                )
        )
        try {
            runBlocking {
                val result = remoteQuestion.addQuestion(
                        question = questionWithAnswers,
                        guide = guide
                )
                when (result) {
                    is TaskResult.Success -> {
                        Log.d(TestingValues.TESTING_TAG, result.data.toString())
                        TestCase.assertTrue(true)
                    }
                    is TaskResult.Error -> {
                        Log.d(TestingValues.TESTING_TAG, result.e.toString())
                        FirebaseCrashlytics.getInstance().recordException(result.e)
                        TestCase.assertTrue(false)
                    }
                }
            }
        } catch (e: Exception) {
            Log.d(TestingValues.TESTING_TAG, e.toString())
            FirebaseCrashlytics.getInstance().recordException(e)
            TestCase.assertTrue(false)
        }

    }

    @Test
    fun getQuestions(){
        try {
            runBlocking {
                val result = remoteQuestion.getGuideQuestions(
                        guide = Guide(remoteId = "0RxKmLPY0ovu1WzzQi0J")
                )
                when (result) {
                    is TaskResult.Success -> {
                        Log.d(TestingValues.TESTING_TAG, result.data.toString())
                        TestCase.assertTrue(true)
                    }
                    is TaskResult.Error -> {
                        Log.d(TestingValues.TESTING_TAG, result.e.toString())
                        FirebaseCrashlytics.getInstance().recordException(result.e)
                        TestCase.assertTrue(false)
                    }
                }
            }
        } catch (e: Exception) {
            Log.d(TestingValues.TESTING_TAG, e.toString())
            FirebaseCrashlytics.getInstance().recordException(e)
            TestCase.assertTrue(false)
        }
    }
}