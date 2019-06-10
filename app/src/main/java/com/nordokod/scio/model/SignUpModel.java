package com.nordokod.scio.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nordokod.scio.controller.SignUpController;
import com.nordokod.scio.entity.User;
import com.nordokod.scio.view.SignupActivity;

public class SignUpModel {
    private FirebaseAuth mAuth;
    private SignUpController signUpController;
    private Context currentContext;

    public SignUpModel(SignUpController signUpController, Context context){
        this.signUpController = signUpController;
        mAuth = FirebaseAuth.getInstance();
        this.currentContext = context;
    }

    public void verifyEmail() {
        // TODO: Hacer la verificacion de que el correo no esté en el base de datos, en caso de que
        //  sí usen este método.
        signUpController.onRejectedEmail();
    }

    public void signUpUser(User user){
        mAuth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        sendVerificationEmail();//enviar correo verificación
                        signUpController.onSuccessSignUp();//avisar controller
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("testeo",e.getMessage());
                        signUpController.onErrorSignUp();
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
