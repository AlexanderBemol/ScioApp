package com.nordokod.scio.model;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.nordokod.scio.controller.LoginController;
import com.nordokod.scio.entity.*;
import com.nordokod.scio.entity.Error;

public class LoginModel {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private FirebaseUser currentUser;
    private Context currentContext;
    private Activity currentActivity;
    private LoginController loginController;
    private GoogleSignInClient mGoogleSignInClient;
    private LoginButton loginButton;
    private CallbackManager mCallbackManager;

    public LoginModel(LoginController loginController){
        this.loginController = loginController;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public void setCurrentContext(Context currentContext) {
        this.currentContext = currentContext;
    }

    public void setLoginButton(LoginButton lb){
        this.loginButton=lb;
    }

    public  void setmCallbackManager(CallbackManager cm){
        this.mCallbackManager=cm;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void initMAuth(){
        mAuth=FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               currentUser=firebaseAuth.getCurrentUser();
            }
        };
        mAuth.addAuthStateListener(mAuthListner);
    }

    public FirebaseUser getCurrentUser(){
        return currentUser;
    }

    public boolean IsUserLogged(){ //El usuario tiene sesión activa?
        currentUser = mAuth.getCurrentUser();
        return (currentUser!=null);
    }

    public void ConfigLogWithGoogle(){ //regresa el google signin client
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1038530395665-l41078qthki82d8tfuhej3o3c2j4hqak.apps.googleusercontent.com")//google console,default-web-id, no funcionó como value
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(currentContext, gso);
    }

    public void loginGoogle(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        currentActivity.startActivityForResult(signInIntent, 123); //RC-SIGNIN
    }

    public void AuthResultGoogle(GoogleSignInAccount acct) {//aquí se revisa la auth de google
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(currentActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.getException() != null) {
                            loginController.loginResult(task.isSuccessful(), new Error(Error.LOGIN_GOOGLE));
                            Log.d("prueba", "error en google: " + task.getException().toString());
                        }
                            else{
                                loginController.loginResult(task.isSuccessful());
                                if(task.getResult().getAdditionalUserInfo().isNewUser()){
                                    loginController.firstConfiguration();
                                }else{
                                    loginController.mainMenu();
                                }
                        }
                    }
                });
    }

    public void onResult(int requestCode, int resultCode, Intent data){//aquí vemos qué pasó con el activity llamado
        if (requestCode == 123) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthResultGoogle(account);
            } catch (Exception e) {
                loginController.loginResult(false, new Error(e.getMessage(), Error.GENERAL));
                Log.d("prueba","error en result: "+e.toString());
            }
        }else{
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void loginWithMail(com.nordokod.scio.entity.User user){
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
            .addOnCompleteListener(currentActivity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.getException() != null){
                        Log.d("testeo",task.getException().getMessage());
                        loginController.loginResult(task.isSuccessful(), new Error(Error.LOGIN_MAIL));}
                    else{
                        loginController.loginResult(task.isSuccessful());
                        if(task.getResult().getAdditionalUserInfo().isNewUser()){
                            loginController.firstConfiguration();
                        }
                    }
                }
            });

    }

    //Facebook
    public void registerCallback(){
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginController.loginResult(true);
                Log.d("prueba", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("prueba", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                loginController.loginResult(false, new Error(Error.LOGIN_FACEBOOK));
                Log.d("prueba", "facebook:onError", error);

            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(currentActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.getException() != null)
                            loginController.loginResult(task.isSuccessful(), new Error(Error.LOGIN_FACEBOOK));
                        else{
                            loginController.loginResult(task.isSuccessful());
                            if(task.getResult().getAdditionalUserInfo().isNewUser()){
                                loginController.firstConfiguration();
                            }else{
                                loginController.mainMenu();
                            }
                        }
                    }
                });
    }



}
