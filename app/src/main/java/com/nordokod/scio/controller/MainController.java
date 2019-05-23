package com.nordokod.scio.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.nordokod.scio.model.MainModel;
import com.nordokod.scio.view.LoginActivity;
import com.nordokod.scio.view.MainActivity;

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
}
