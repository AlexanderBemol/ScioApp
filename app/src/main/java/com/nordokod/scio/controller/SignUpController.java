package com.nordokod.scio.controller;

import android.app.Activity;
import android.content.Context;
import com.nordokod.scio.entity.User;
import com.nordokod.scio.model.SignUpModel;

public class SignUpController {
    SignUpModel signModel;
    //SignUpActivity signActivity;
    User user;
    Activity currentActivity;
    Context currentContext;
    public SignUpController(Context cCon,Activity cAct){
        signModel=new SignUpModel(this,cAct,cCon);
        user=new User();
        currentActivity=cAct;
        currentContext=cCon;
    }
    public void signUpUser(String email,String password1,String password2){
        if(password1.length()>=8){
            if(password1.equals(password2)){
                user.setEmail(email);
                user.setPassword(password1);
                signModel.signUpUser(user);
            }else{
                //no son iguales las contraseñas
            }
        }else{
            //contraseña muy corta
        }
    }

    public void correctSignUp(){

    }
    public void incorrectSignUp(String error){

    }
}
