package com.nordokod.scio.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.nordokod.scio.entity.Error;
import com.nordokod.scio.entity.User;
import com.nordokod.scio.model.LoginModel;
import com.nordokod.scio.view.FirstConfigurationActivity;
import com.nordokod.scio.view.LoginActivity;
import com.nordokod.scio.view.MainActivity;
import com.nordokod.scio.view.SignupActivity;

public class LoginController{
    private LoginModel logModel;
    private LoginActivity logActivity;
    private User user;
    private Activity currentActivity;
    private Context currentContext;

    public LoginController() {
        logModel    =   new LoginModel(this);
        user        =   new User();
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

    public void loginWithMail(String mail, String password){
        if (mail.length() != 0 && password.length() != 0){
            user.setPassword(password);
            user.setEmail(mail);

            logModel.loginWithMail(user);
        }else {
            logActivity.showErrorNoticeDialog(new Error(Error.EMPTY_FIELD));
        }
    }

    public void loginResult(Boolean result, Error error){ //con error
        if (result){
            logActivity.showSuccessNoticeDialog(null);

        }
        else {
            logActivity.showErrorNoticeDialog(error);
        }
    }

    public void loginResult(Boolean result){ //sin error
        if (result){
            logActivity.showSuccessNoticeDialog(null);
            mainMenu();
        }
    }

    public void confGoogle(){
        logModel.ConfigLogWithGoogle();
    }

    public void loginGoogle(){
        logModel.loginGoogle();
    }

    public void onResult(int requestCode, int resultCode, Intent data){
        logModel.onResult(requestCode,resultCode,data);
    }

    public void confFB(CallbackManager cb, LoginButton lb){
        logModel.setLoginButton(lb);
        logModel.setmCallbackManager(cb);
        logModel.registerCallback();
    }
    public void firstConfiguration(){
        Intent intent= new Intent(currentContext, FirstConfigurationActivity.class);
        currentContext.startActivity(intent);
        logActivity.finish();
    }
    public void mainMenu(){
        Intent menu= new Intent(currentContext, MainActivity.class);
        currentContext.startActivity(menu);
        logActivity.finish();
    }
    public boolean IsUserLogged(){
        return logModel.IsUserLogged();
    }

    public void signup() {
        Intent intent = new Intent(logActivity, SignupActivity.class);
        logActivity.startActivity(intent);
    }
}
