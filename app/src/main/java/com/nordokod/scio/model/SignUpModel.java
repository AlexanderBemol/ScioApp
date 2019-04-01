package com.nordokod.scio.model;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nordokod.scio.controller.SignUpController;
import com.nordokod.scio.entity.User;

public class SignUpModel {
    private FirebaseAuth mAuth;
    SignUpController SignController;
    Activity currentActivity;
    Context currentContext;

    public SignUpModel(SignUpController SignC,Activity cAct,Context cCon){
        this.SignController=SignC;
        mAuth = FirebaseAuth.getInstance();
        currentActivity=cAct;
        currentContext=cCon;
    }
    public void signUpUser(User user){
        mAuth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword())
                .addOnCompleteListener(currentActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {//se registró correctamente
                            sendVerificationEmail();//enviar correo verificación
                            SignController.correctSignUp();//avisar controller
                        } else {
                            if(task.getException()!=null) {//evitar nullPointerException
                                SignController.incorrectSignUp(task.getException().toString());//avisar controller con error
                            }
                        }
                    }
                });
    }
    private void sendVerificationEmail()
    {
        FirebaseUser fbsUser = mAuth.getCurrentUser();
        if (fbsUser != null) {
            fbsUser.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                             //se envió
                            }
                        }
                    });
        }
    }

}
