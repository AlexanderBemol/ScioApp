package com.nordokod.scio.Model;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.nordokod.scio.R;


public class LoginModel {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private FirebaseUser currentUser;
    private Context currentContext;
    private Activity currentActivity;

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }
    public FirebaseAuth getmAuth() { return mAuth; }

    public void setCurrentContext(Context currentContext) {
        this.currentContext = currentContext;
    }

    public boolean userIsLogged(){ //El usuario tiene sesión activa?
        mAuth.addAuthStateListener(mAuthListner);
        currentUser = mAuth.getCurrentUser();
        return (currentUser!=null);
    }

    public GoogleSignInClient ConfigLogWithGoogle(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(currentContext.getString(R.string.default_web_client_id))//google console
                .requestEmail()
                .build();

        return GoogleSignIn.getClient(currentContext, gso);
    }
    private Boolean firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        final Boolean[] b = new Boolean[1];
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(currentActivity, new OnCompleteListener<AuthResult>() {
                   @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       b[0]=task.isSuccessful();
                    }
                });
        return b[0];
    }
}
