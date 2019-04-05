package com.nordokod.scio.controller;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.nordokod.scio.R;
import com.nordokod.scio.entity.Error;
import com.nordokod.scio.model.FirstConfigurationModel;
import com.nordokod.scio.view.FirstConfigurationActivity;

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
        error.setDescriptionResource(R.string.message_save_error);
        error.setDescriptionText("Algo salió mal");
        fcActivity.showErrorNoticeDialog(error);
    }

}
