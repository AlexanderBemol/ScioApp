package com.nordokod.scio.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Editable;
import android.util.Log;


import com.nordokod.scio.entity.Error;

import com.nordokod.scio.entity.Guide;
import com.nordokod.scio.entity.MultipleChoiceQuestion;
import com.nordokod.scio.entity.Question;
import com.nordokod.scio.model.MainModel;
import com.nordokod.scio.view.LoginActivity;
import com.nordokod.scio.view.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainController {
    private MainActivity mainActivity;
    private MainModel mainModel;
    private Context currentContext;

    public MainController(MainActivity mActivity,Context context){
        this.mainActivity=mActivity;
        this.currentContext=context;
        this.mainModel=new MainModel(this,mActivity,context);
    }

    public void requestPhoto(){
        mainModel.requestPhoto();
    }

    public void setDefaultUserPhoto(Bitmap userPhoto) {
        mainActivity.setUserPhoto(userPhoto);
    }

    public void setDefaultScioPhoto() {
        mainActivity.setDefaultUserPhoto();
    }

    public void logOut() {
        mainModel.logOut();
        Intent intent = new Intent(currentContext, LoginActivity.class);
        currentContext.startActivity(intent);
    }

    public String getName(){
        return mainModel.getName();
    }

    public void createGuide(int category_selected_id, Editable topic, String date_selected, String time_selected){
        try {
            Error error;
            if (category_selected_id == 0) {
                error=new Error(Error.EMPTY_FIELD);
                showError(error);
            }else{
                if (topic.length() == 0) {
                    error=new Error(Error.EMPTY_FIELD);
                    showError(error);
                }else{
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.ENGLISH);
                    String datetime = date_selected + " " + time_selected;
                    Log.d("testeo",datetime);
                    Date date = sdf.parse(datetime);
                    Date dateToday = new Date();
                    if (date.before(dateToday)){
                        Log.d("testeo","fechamal");
                        error=new Error(Error.GUY_FROM_THE_FUTURE);
                        showError(error);
                    }else{
                        String finalTopic=topic.toString();
                        Guide guide=new Guide();
                        guide.setCategory(category_selected_id);
                        guide.setDatetime(date);
                        guide.setTopic(finalTopic);
                        mainModel.createGuide(guide);
                    }
                }

            }

        }
        catch (NullPointerException e){
            Error error=new Error(Error.GUY_FROM_THE_FUTURE);
            showError(error);
        }
        catch (Exception e){
            Error error=new Error(Error.GENERAL);
            showError(error);
            Log.d("testeo",e.getMessage());
        }
    }

    public void showError(Error error){
        mainActivity.showErrorNoticeDialog(error);
    }

    public void succesUpload() {
        mainActivity.showSuccessNoticeDialog(null);
    }

    public ArrayList<Guide> getListOfGuides(int category) {
        ArrayList<Guide> guides=new ArrayList<>();
        for(Guide guide:mainModel.getListOfGuides()){
            if(guide.getCategory()==category)
                guides.add(guide);
        }
        return guides;
    }

    public void onCloseFragment(String tag) {
        mainActivity.onCloseFragment(tag);
    }

    public void loadGuides(){
        mainModel.loadGuides();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Para abrir los dialog.~~~~~~~~~
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public void onNewQuestionDialog(Guide guide) {
        mainActivity.onNewQuestionDialog(guide);
    }

    public void onNewMultipleChoiceQuestionDialog(Guide guide) {
        mainActivity.onNewMultipleChoiceQuestionDialog(guide);
    }

    public void onNewOpenAnswerQuestionDialog(Guide guide) {
        mainActivity.onNewOpenAnswerQuestionDialog(guide);
    }

    public void onNewTrueFalseQuestionDialog(Guide guide) {
        mainActivity.onNewTrueFalseQuestionDialog(guide);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Para guardar la preguntas.~~~~~
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public void onSaveMultipleChoiceQuestion(Guide guide, String question,
                                             String option_1, String option_2, String option_3, String option_4,
                                             boolean is_correct_1, boolean is_correct_2, boolean is_correct_3, boolean is_correct_4) {
        if (!question.equals("")) {
            if (!option_1.equals("") && !option_2.equals("") && !option_3.equals("") && !option_4.equals("")) {
                if (!is_correct_1 && !is_correct_2 && !is_correct_3 && !is_correct_4) {
                    onUnselectedAnswer();
                } else {
                    mainModel.onSaveMultipleChoiceQuestion(guide, question,
                            option_1, option_2, option_3, option_4,
                            is_correct_1, is_correct_2, is_correct_3, is_correct_4);
                }
            } else {
                onEmptyField();
            }
        } else {
            onEmptyField();
        }
    }

    public void onSaveTrueFalseQuestion(Guide guide, String question, boolean answer) {
        if (!question.equals(""))
            mainModel.onSaveTrueFalseQuestion(guide, question, answer);
        else
            onEmptyField();
    }

    public void onSaveOpenAnswerQuestion(Guide guide, String question, String answer) {
        if (!question.equals("") && !answer.equals(""))
            mainModel.onSaveOpenAnswerQuestion(guide, question, answer);
        else
            onEmptyField();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Para mostrar los mensajes.~~~~~
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public void onSuccessSaveQuestion() {
        mainActivity.onSuccessSaveQuestion();
    }

    public void onErrorSaveQuestion() {
        mainActivity.onErrorSaveQuestion();
    }

    public void onEmptyField() {
        mainActivity.onEmptyField();
    }

    public void onUnselectedAnswer() {
        mainActivity.onUnselectedAnswer();
    }

    public void checkConnectionMode(){
        mainModel.checkConnectionMode();
    }

    public void refreshGuides() {
        mainActivity.refreshGuides();
    }
}
