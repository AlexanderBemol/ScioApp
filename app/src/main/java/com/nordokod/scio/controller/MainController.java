package com.nordokod.scio.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Editable;
import android.util.Log;


import com.nordokod.scio.entity.Error;

import com.nordokod.scio.model.MainModel;
import com.nordokod.scio.view.LoginActivity;
import com.nordokod.scio.view.MainActivity;

import java.text.SimpleDateFormat;
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
                        mainModel.createGuide(category_selected_id,finalTopic,date);
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
            Log.d("testeo",e.getMessage().toString());
        }
    }
    public void showError(Error error){
        mainActivity.showErrorNoticeDialog(error);
    }

    public void succesUpload() {
        mainActivity.showSuccessNoticeDialog(null);
    }
}
