package com.nordokod.scio.controller;

import android.app.Activity;
import android.content.Context;

import com.nordokod.scio.entity.Error;
import com.nordokod.scio.entity.User;
import com.nordokod.scio.model.SignUpModel;
import com.nordokod.scio.view.SignupActivity;

public class SignUpController {
    private SignUpModel signModel;
    private SignupActivity signupActivity;
    private User user;
    private Activity currentActivity;
    private Context currentContext;
    public SignUpController(Context cCon,Activity cAct, SignupActivity activity){
        signModel=new SignUpModel(this,cAct,cCon);
        user=new User();
        currentActivity=cAct;
        currentContext=cCon;
        signupActivity=activity;
    }
    public void signUpUser(String email,String password1,String password2){
        if(password1.length()>=8){
            if(password1.equals(password2)){
                user.setEmail(email);
                user.setPassword(password1);
                signModel.signUpUser(user);
            }else{
                Error error=new Error(Error.WHEN_LOADING);
                error.setDescriptionText("Las contraseñas no coinciden");
                signupActivity.showErrorNoticeDialog(error);
            }
        }else{
            Error error=new Error(Error.WHEN_LOADING);
            error.setDescriptionText("Las contraseña debe ser de almenos 8 caracteres");
            signupActivity.showErrorNoticeDialog(error);
        }
    }

    public void correctSignUp(){
        signupActivity.showSuccessNoticeDialog("El registro ha sido exitoso");
    }
    public void incorrectSignUp(Error error){
        signupActivity.showErrorNoticeDialog(error);
    }
}
