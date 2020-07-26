package com.nordokod.scio.source

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.nordokod.scio.kt.constants.Testing
import com.nordokod.scio.kt.constants.enums.GuideCategory
import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.model.source.FirebaseGuide
import com.nordokod.scio.kt.utils.TaskResult
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.*

class GuidesFirebaseTest {
    private val firebaseDatabase = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseGuides = FirebaseGuide(firebaseDatabase,firebaseAuth)

    @Test
    fun createGuide(){
        runBlocking {
            val result = firebaseGuides.createGuide(
                    Guide(
                            id = "",
                            topic = "TEST",
                            category = GuideCategory.OTHERS.code,
                            online = true,
                            activated = true,
                            testDate = Date(),
                            creationDate = Date(),
                            updateDate =  Date(),
                            creationUser = "testing-001",
                            updateUser = "testing-001"
                    )
            )
            assertTrue(result is TaskResult.Success)
            Log.d(Testing.TESTING_TAG,result.toString())
        }
    }

    @Test
    fun getUserGuidesOnline(){
        runBlocking {
            val result = firebaseGuides.getUserGuides()
            assertTrue(result is TaskResult.Success)
            Log.d(Testing.TESTING_TAG,result.toString())
        }
    }


}