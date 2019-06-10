package com.nordokod.scio.controller;

import android.content.Context;

import com.nordokod.scio.entity.Error;
import com.nordokod.scio.entity.User;
import com.nordokod.scio.model.SignUpModel;
import com.nordokod.scio.view.SignupActivity;

import es.dmoral.toasty.Toasty;

public class SignUpController {
    private SignUpModel signUpModel;
    private SignupActivity signupActivity;
    private User user;
    private Context currentContext;

    public SignUpController(Context context, SignupActivity signupActivity){
        signUpModel = new SignUpModel(this, context);
        this.currentContext = context;
        this.signupActivity = signupActivity;
    }

    public void signUpUser(String email, String password1, String password2){
        if (!email.equals("") && !password1.equals("") && !password2.equals("")) { // Comprobar campos vacios
            if (password1.length() >= 8) { // Validar tamaño de la contraseña
                if (password1.equals(password2)) { // Validar que sean la misma contraseña
                    signupActivity.onShowProgressDialog();

                    user = new User();
                    user.setEmail(email);
                    user.setPassword(password1);

                    signUpModel.signUpUser(user);
                } else
                    signupActivity.onErrorMatchPasswords();
            } else
                signupActivity.onInvalidPassword();
        } else
            signupActivity.onEmptyFields();
    }

    public void onSuccessSignUp(){
        signupActivity.onSuccessSignUp();
    }

    public void onErrorSignUp(){
        signupActivity.onErrorSignUp();
    }

    public void onRejectedEmail() {
        signupActivity.onRejectedEmail();
    }
}
