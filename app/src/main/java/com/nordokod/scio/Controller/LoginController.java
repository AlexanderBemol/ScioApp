package com.nordokod.scio.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.nordokod.scio.Entidad.User;
import com.nordokod.scio.Model.LoginModel;
import com.nordokod.scio.View.LoginActivity;

public class LoginController{
    LoginModel logModel;
    LoginActivity logActivity;
    User user;
    Activity currentActivity;
    Context currentContext;
    public LoginController() {
        logModel=new LoginModel(this);
        user=new User();
    }
    public void configController(Activity activity,Context context,LoginActivity logActivity){//dar los datos necesarios al controller
        currentActivity=activity;
        currentContext=context;
        this.logActivity=logActivity;
        logModel.setCurrentContext(currentContext);
        logModel.setCurrentActivity(currentActivity);
    }
    public void initializeFirebase(){
        logModel.initMAuth();
    }
    public void signinWithMail(String mail,String password){
        if (mail.length()!=0&&password.length()!=0){
            user.setPassword(password);
            user.setEmail(mail);
            logModel.signinWithMail(user);
        }else {
            logActivity.camposIncompletos();
        }
    }
    public void signinResult(Boolean result,String error){ //con error
        if (result){
            logActivity.correctoUsuario();
            //todobien
        }
        else {
            logActivity.errorUsuario(error);
            //falló
        }
    }
    public void signinResult(Boolean result){//sin error
        if (result){
            logActivity.correctoUsuario();
            //todobien
        }
        else{
            logActivity.errorUsuario("Error al iniciar sesión");
            //falló
        }
    }
    public void confGoogle(){
        logModel.ConfigLogWithGoogle();
    }
    public void signinGoogle(){
        logModel.signinGoogle();
    }
    public void onResult(int requestCode, int resultCode, Intent data){
        logModel.onResult(requestCode,resultCode,data);
    }
    public void confFB(CallbackManager cb, LoginButton lb){
        logModel.setLoginButton(lb);
        logModel.setmCallbackManager(cb);
        logModel.registerCallback();
    }
}
