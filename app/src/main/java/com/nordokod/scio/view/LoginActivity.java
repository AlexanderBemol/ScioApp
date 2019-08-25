package com.nordokod.scio.view;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.nordokod.scio.entity.Error;
import com.nordokod.scio.R;
import com.nordokod.scio.model.User;
import com.victor.loading.newton.NewtonCradleLoading;

import java.util.Arrays;
import java.util.Objects;
import com.nordokod.scio.constants.*;

import javax.annotation.Nullable;

public class LoginActivity extends AppCompatActivity implements BasicActivity {
    // Botones de Inicio de Sesion y Registrarse
    private AppCompatButton BTN_Login, BTN_Signup;
    // Facebook and Google Buttons
    private AppCompatImageButton BTN_Google, BTN_FB;
    private LoginButton BTN_Facebook;
    // Recover password TextView
    private AppCompatTextView txtRecuperar;
    // User and Password EditTexts
    private AppCompatEditText ET_Mail, ET_Password;
    //Facebook
    private CallbackManager callbackManager;
    //Google
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient mGoogleSignInClient;
    //Model
    private User userModel;
    // Animations
    private Animation press;
    // Dialogs
    private Dialog loginErrorDialog, noticeDialog;
    private static final int NOTICE_DIALOG_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponents();
        initAnimations();
        initListeners();
        if(userModel.isUserLogged())goToMainView();
    }

    private void showLoginLoadingDialog(){
        if (noticeDialog == null)
            noticeDialog = new Dialog(this);
        else if (noticeDialog.isShowing()) {
            noticeDialog.dismiss();
        }

        noticeDialog.setContentView(R.layout.dialog_progress);
        noticeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        noticeDialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        noticeDialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;
        noticeDialog.getWindow().getAttributes().windowAnimations = R.style.NoticeDialogAnimation;

        NewtonCradleLoading loading = noticeDialog.findViewById(R.id.loading);

        noticeDialog.show();
        loading.start();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RequestCode.RC_GOOGLE.getCode()){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                userModel.signInWithGoogle(account).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if(authResult.getAdditionalUserInfo().isNewUser()){
                            goToFirstConfigurationView();
                        }
                        else{
                            goToMainView();
                        }
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        showError(new Exception());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showError(e);
                    }
                });
            } catch (Exception e) {
                Log.d("testing","error en result: "+e.toString());
                dismissProgressDialog();
            }
        }
        else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void initComponents(){
        BTN_Login = findViewById(R.id.BTN_Login);
        BTN_Signup = findViewById(R.id.BTN_Signup);

        BTN_FB = findViewById(R.id.BTN_Facebook);
        BTN_Google = findViewById(R.id.BTN_Google);

        txtRecuperar    = findViewById(R.id.TV_Forgot_Password);

        ET_Mail = findViewById(R.id.ET_Mail);
        ET_Password = findViewById(R.id.ET_Password);

        callbackManager = CallbackManager.Factory.create();

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        userModel = new User();
    }

    @Override
    public void initListeners(){
        //Iniciar Sesión
        BTN_Login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                BTN_Login.startAnimation(press);
                //programar lógica de iniciar sesión, usar userModel.signInWithMail, revisar vacíos etc.
                //showLoginLoadingDialog();

            }
        });

        //Google
        BTN_Google.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                BTN_Google.startAnimation(press);
                showLoginLoadingDialog();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent,RequestCode.RC_GOOGLE.getCode());
            }
        });

        //Facebook
        BTN_FB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showLoginLoadingDialog();
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email","public_profile","user_birthday"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        userModel.signInWithFacebook(loginResult.getAccessToken()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                if(authResult.getAdditionalUserInfo().isNewUser()){
                                    goToFirstConfigurationView();
                                }else{
                                    goToMainView();
                                }
                            }
                        }).addOnCanceledListener(new OnCanceledListener() {
                            @Override
                            public void onCanceled() {
                                showError(new Exception());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                showError(e);
                            }
                        });
                    }
                    @Override
                    public void onCancel() {
                        showError(new Exception());
                    }
                    @Override
                    public void onError(FacebookException error) {
                        showError(new Exception());
                    }
                });
            }
        });

        //Registrarse
        BTN_Signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                BTN_Signup.startAnimation(press);
                goToSignUpView();
            }
        });

        //Recuperar Contraseña
        txtRecuperar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //activity restore password
            }
        });
    }

    @Override
    public void showErrorNoticeDialog(Error error) {

        if (noticeDialog == null)
            noticeDialog = new Dialog(this);
        else if (noticeDialog.isShowing()) {
            noticeDialog.dismiss();
        }

        noticeDialog.setContentView(R.layout.dialog_error);
        Objects.requireNonNull(noticeDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        noticeDialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        noticeDialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;
        noticeDialog.getWindow().getAttributes().windowAnimations = R.style.NoticeDialogAnimation;

        AppCompatTextView errorMessage  = noticeDialog.findViewById(R.id.TV_Message);
        AppCompatImageView image        = noticeDialog.findViewById(R.id.IV_Error);

        if (error.getDescriptionResource() != 0) {
            AppCompatTextView errorDescription = noticeDialog.findViewById(R.id.TV_Description);
            errorDescription.setVisibility(View.VISIBLE);
            errorDescription.setText(error.getDescriptionResource());
        }else if (error.getDescriptionText() != null) {
            AppCompatTextView errorDescription = noticeDialog.findViewById(R.id.TV_Description);
            errorDescription.setVisibility(View.VISIBLE);
            errorDescription.setText(error.getDescriptionText());
        }

        switch (error.getType()) {
            case Error.EMPTY_FIELD:
                image.setVisibility(View.GONE);
                errorMessage.setText(R.string.message_emptyfields_error);
                break;
            case Error.LOGIN_MAIL:
                image.setImageResource(R.drawable.ic_mail);
                errorMessage.setText(R.string.error_mail);
                break;
            case Error.LOGIN_FACEBOOK:
                image.setImageResource(R.drawable.ic_facebook);
                errorMessage.setText(R.string.error_facebook);
                break;
            case Error.LOGIN_GOOGLE:
                image.setImageResource(R.drawable.ic_google);
                errorMessage.setText(R.string.error_google);
                break;
            case Error.CONNECTION:
                errorMessage.setText(R.string.message_connection_error);
                break;
            default:
                image.setVisibility(View.GONE);
                errorMessage.setText(R.string.message_error);
                break;
        }
        noticeDialog.show();

        Handler handler;
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                if (noticeDialog != null) {
                    noticeDialog.cancel();
                    noticeDialog.dismiss();
                    noticeDialog = null;
                }
            }
        }, NOTICE_DIALOG_TIME);

        handler = null;
    }

    @Override
    public void showSuccessNoticeDialog(String task) {
        if (noticeDialog == null)
            noticeDialog = new Dialog(this);
        else if (noticeDialog.isShowing()) {
            noticeDialog.dismiss();
        }

        noticeDialog.setContentView(R.layout.dialog_success);
        noticeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        noticeDialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        noticeDialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;
        noticeDialog.getWindow().getAttributes().windowAnimations = R.style.NoticeDialogAnimation;

        AppCompatTextView message = noticeDialog.findViewById(R.id.TV_Message);
        message.setText(R.string.message_login_success);

        noticeDialog.show();

        Handler handler;
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                noticeDialog.cancel();
                noticeDialog.dismiss();
                noticeDialog = null;
            }
        }, 1000);

        handler = null;
    }

    private void initAnimations(){
        press = AnimationUtils.loadAnimation(this, R.anim.press);
    }

    private void goToFirstConfigurationView(){
        Intent firstConfigurationIntent = new Intent(this,FirstConfigurationActivity.class);
        startActivity(firstConfigurationIntent);
        dismissProgressDialog();
    }

    private void goToMainView(){
        Intent mainIntent = new Intent(this,MainActivity.class);
        startActivity(mainIntent);
        dismissProgressDialog();
    }
    private void goToSignUpView(){
        Intent signUpIntent = new Intent(this,SignupActivity.class);
        startActivity(signUpIntent);
        dismissProgressDialog();
    }
    private void showError(Exception exception){
        Log.d("testing",exception.getLocalizedMessage());
        ErrorMessage errorMessage;
        if(exception instanceof FirebaseAuthInvalidCredentialsException){
            errorMessage=ErrorMessage.E_03;
        }
        else if(exception instanceof FirebaseAuthInvalidUserException){
            errorMessage=ErrorMessage.E_03;
        }
        else if(exception instanceof FirebaseAuthUserCollisionException){
            errorMessage=ErrorMessage.E_13;
        }
        else if(exception instanceof FirebaseNetworkException){
            errorMessage=ErrorMessage.E_02;
        }
        dismissProgressDialog();
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    private void dismissProgressDialog() {
        if (noticeDialog != null && noticeDialog.isShowing()) {
            noticeDialog.dismiss();
        }
    }
}
