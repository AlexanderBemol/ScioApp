package com.nordokod.scio;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.nordokod.scio.entity.Guide;
import com.nordokod.scio.model.Question;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void checkQuestions() throws InterruptedException {
        Question questionM = new Question();
        Guide guide = new Guide(1,"ox6nRhhkMFUrVsmYzK5HfP1zT9j2","test","",true,true, new Date());
        final Object syncObject = new Object();
        questionM.getSnapshotQuestionsOfGuide(guide)
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Assert.assertTrue(true);
                    synchronized (syncObject){
                        syncObject.notify();
                    }
                })
                .addOnFailureListener(e ->{
                    fail();
                    synchronized (syncObject){
                        syncObject.notify();
                    }
                });
        synchronized (syncObject){
            syncObject.wait();
        }

    }
}
