package com.nordokod.scio.Model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginModel {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private FirebaseUser currentUser;

    public FirebaseAuth getmAuth() { return mAuth; }

    public boolean userIsLogged(){ //El usuario tiene sesión activa?
        mAuth.addAuthStateListener(mAuthListner);
        currentUser = mAuth.getCurrentUser();
        return (currentUser!=null);
    }

}
