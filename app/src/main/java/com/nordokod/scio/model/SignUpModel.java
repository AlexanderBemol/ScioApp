package com.nordokod.scio.model;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nordokod.scio.controller.SignUpController;
import com.nordokod.scio.entity.Error;
import com.nordokod.scio.entity.User;

public class SignUpModel {
    private FirebaseAuth mAuth;
    private SignUpController SignController;
    private Activity currentActivity;
    private Context currentContext;

    public SignUpModel(SignUpController SignC,Activity cAct,Context cCon){
        this.SignController=SignC;
        mAuth = FirebaseAuth.getInstance();
        currentActivity=cAct;
        currentContext=cCon;
    }
    public void signUpUser(User user){
        mAuth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        sendVerificationEmail();//enviar correo verificación
                        SignController.correctSignUp();//avisar controller
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("testeo",e.getMessage());
                        SignController.incorrectSignUp(new Error(Error.WHEN_LOADING));
                    }
                })
        ;
    }
    private void sendVerificationEmail()
    {
        FirebaseUser fbsUser = mAuth.getCurrentUser();
        if (fbsUser != null) {
            fbsUser.sendEmailVerification();
        }
    }

}
