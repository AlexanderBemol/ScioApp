package com.nordokod.scio.controller;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.nordokod.scio.R;
import com.nordokod.scio.entity.App;
import com.nordokod.scio.entity.Error;
import com.nordokod.scio.model.FirstConfigurationModel;
import com.nordokod.scio.view.FirstConfigurationActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class FirstConfigurationController {
    private Context currentContext;
    private Activity currentActivity;
    private FirstConfigurationModel fcModel;
    private FirstConfigurationActivity fcActivity;
    private CircleImageView CIV_Photo;
    public  FirstConfigurationController(Context context,Activity activity,FirstConfigurationActivity fca){
        this.currentActivity=activity;
        this.currentContext=context;
        this.fcActivity=fca;
        this.fcModel=new FirstConfigurationModel(this,activity,context);
    }
    //petición para descargar foto del usuario
    public void requestPhoto(CircleImageView civ){
        this.CIV_Photo=civ;
        fcModel.requestPhoto();
    }
    //poner la foto por defecto de scio
    public void setDefaultScioPhoto(){
        CIV_Photo.setImageResource(R.drawable.default_photo);
    }
    //poner la foto del usuario
    public void setDefaultUserPhoto(Bitmap photo){
        CIV_Photo.setImageBitmap(photo);
    }

    //seleccionar foto
    public void photoFromStorage(){
        fcModel.photoFromStorage();
    }
    //recortar foto
    public void trimPhoto(Uri photo){
        fcModel.trimPhoto(photo);
    }
    public void uploadPhoto(Uri photo){
        fcModel.uploadPhoto(photo);
    }
    public void uploadComplete(){
        fcActivity.updatePhoto();
    }
    public void uploadCanceled(){
        Error error=new Error();
        error.setType(Error.WHEN_SAVING_ON_DATABASE);
        fcActivity.showErrorNoticeDialog(error);
    }

    public String getName(){
        return fcModel.getName();
    }


    public boolean validateFields(String name, String birthday,int education){
        return fcModel.validateFields(name,birthday,education);
    }

    public ArrayList<App> getListOfApps() {
        return fcModel.getListOfApps();
    }

    public boolean onStateChanged(String packagePath, boolean isChecked) {
        return fcModel.onStateChanged(packagePath,isChecked);
    }

    public void showError(Error error) {
        fcActivity.showErrorNoticeDialog(error);
    }
    public void saveConfiguration(String name, String birthday, int education){
        fcModel.saveConfiguration(name,birthday,education);
    }
    public void configurationSaved(){

    }

}
