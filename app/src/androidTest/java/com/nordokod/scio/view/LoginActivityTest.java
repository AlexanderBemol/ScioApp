package com.nordokod.scio.view;

import android.app.Activity;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.nordokod.scio.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.junit.Assert.*;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.mockito.Mockito.verify;


@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    private ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);


    @Test
    public void incorrectlyAccesEmptyField1(){
        //ambos campos vacíos
        LoginActivity login=mActivityRule.launchActivity(new Intent());
        String expected = login.getResources().getString(R.string.message_emptyfields_error);
        onView(withId(R.id.ET_Mail))
                .perform(typeText(""),closeSoftKeyboard());
        onView(withId(R.id.ET_Password))
                .perform(typeText(""),closeSoftKeyboard());
        onView(withId(R.id.BTN_Login))
                .perform(click());
        onView(withId(R.id.TV_Message))
                .check(matches(withText(expected)));
        mActivityRule.finishActivity();
    }
    @Test
    public void incorrectlyAccesEmptyField2(){
        //mail vacío
        LoginActivity login=mActivityRule.launchActivity(new Intent());
        String expected = login.getResources().getString(R.string.message_emptyfields_error);
        onView(withId(R.id.ET_Mail))
                .perform(typeText(""),closeSoftKeyboard());
        onView(withId(R.id.ET_Password))
                .perform(typeText("1235678"),closeSoftKeyboard());
        onView(withId(R.id.BTN_Login))
                .perform(click());
        onView(withId(R.id.TV_Message))
                .check(matches(withText(expected)));
        mActivityRule.finishActivity();
    }

    @Test
    public void incorrectlyAccesEmptyField3(){
        //password vacío
        LoginActivity login=mActivityRule.launchActivity(new Intent());
        String expected = login.getResources().getString(R.string.message_emptyfields_error);
        onView(withId(R.id.ET_Mail))
                .perform(typeText("aleex1742@gmail.com"),closeSoftKeyboard());
        onView(withId(R.id.ET_Password))
                .perform(typeText(""),closeSoftKeyboard());
        onView(withId(R.id.BTN_Login))
                .perform(click());
        onView(withId(R.id.TV_Message))
                .check(matches(withText(expected)));
        mActivityRule.finishActivity();
    }

    @Test
    public void incorrectlyAccesInvalidUser1(){
        LoginActivity login=mActivityRule.launchActivity(new Intent());
        String expected = login.getResources().getString(R.string.error_mail);
        onView(withId(R.id.ET_Mail))
                .perform(typeText("aleex1742@gmail.com"),closeSoftKeyboard());
        onView(withId(R.id.ET_Password))
                .perform(typeText("12345678"),closeSoftKeyboard());
        onView(withId(R.id.BTN_Login))
                .perform(click());
        onView(withId(R.id.TV_Message))
                .check(matches(withText(expected)));
        mActivityRule.finishActivity();
    }

    @Test
    public void correctlyAccesUser(){
        LoginActivity login=mActivityRule.launchActivity(new Intent());
        String expected = login.getResources().getString(R.string.message_login_success);
        onView(withId(R.id.ET_Mail))
                .perform(typeText("elpinoles@gmail.com"),closeSoftKeyboard());
        onView(withId(R.id.ET_Password))
                .perform(typeText("Pinolazo123*0"),closeSoftKeyboard());
        onView(withId(R.id.BTN_Login))
                .perform(click());
        onView(withId(R.id.TV_Message))
                .check(matches(withText(expected)));
        mActivityRule.finishActivity();
    }

}