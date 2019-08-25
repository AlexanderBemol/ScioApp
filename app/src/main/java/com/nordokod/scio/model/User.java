package com.nordokod.scio.model;


import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
public class User {
    private FirebaseAuth mAuth;

    public User() {
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Revisa si el usuario está logeado
     * @return boolan con el resultado
     */
    public boolean isUserLogged() {
        return mAuth.getCurrentUser() != null;
    }
    /**
     * Registrar Usuario por Correo Electrónio
     * @param user Entidad del usuario
     */
    public Task<AuthResult> signUpWithMail(com.nordokod.scio.entity.User user) {
        return mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword());
    }

    /**
     * Acceder con correo y contraseña
     * @param user
     */
    public Task<AuthResult> signInWithMail(com.nordokod.scio.entity.User user){
        return mAuth.signInWithEmailAndPassword(user.getEmail(),user.getPassword());
    }

    /**
     * Acceder con cuenta de google
     * @param googleSignInAccount dado en el controlador
     */
    public Task<AuthResult> signInWithGoogle(GoogleSignInAccount googleSignInAccount) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        return mAuth.signInWithCredential(authCredential);
    }

    /**
     * Acceder con cuenta de google
     * @param accessToken token otorgado por fb
     */
    public Task<AuthResult> signInWithFacebook(AccessToken accessToken){
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        return mAuth.signInWithCredential(credential);
    }

    /**
     * Envíar correo de verificación al correo
     */
    public Task<Void> sendVerificationMail(){
        if(mAuth.getCurrentUser()!=null){
            return mAuth.getCurrentUser().sendEmailVerification();
        }
        else{
            return null;
        }
    }

    /**
     * Cerrar Sesión
     */
    public void logOut(){
        mAuth.signOut();
    }

}
