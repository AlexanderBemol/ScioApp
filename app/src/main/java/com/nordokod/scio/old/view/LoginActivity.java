package com.nordokod.scio.old.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.view.Gravity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.nordokod.scio.R;
import com.nordokod.scio.old.constants.RequestCode;
import com.nordokod.scio.old.constants.UserOperations;
import com.nordokod.scio.old.entity.InputDataException;
import com.nordokod.scio.old.entity.OperationCanceledException;
import com.nordokod.scio.old.model.User;
import com.nordokod.scio.old.process.UpdateCheck;
import com.nordokod.scio.old.process.UserMessage;
import com.victor.loading.newton.NewtonCradleLoading;

import java.util.Arrays;
import java.util.Objects;

import com.nordokod.scio.constants.*;

public class LoginActivity extends AppCompatActivity implements BasicActivity {
    // Botones de Inicio de Sesion y Registrarse
    private AppCompatButton BTN_Login, BTN_Signup;
    // Facebook and Google Buttons
    private AppCompatImageButton BTN_Google, BTN_FB;
    // Recover password TextView
    private AppCompatTextView TV_Forgot_Password;
    // User and Password EditTexts
    private AppCompatEditText ET_Mail, ET_Password;
    //Facebook
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    //Model
    private User userModel;
    // Animations
    private Animation press;
    // Dialogs
    private Dialog  noticeDialog;
    //Mensajes de usuario
    private UserMessage userMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponents();
        initAnimations();
        initListeners();
        if(userModel.isUserLogged()){
            if(userModel.isEmailVerified())goToMainView();
            else goToVerifyMail();
        }

    }

    private void showLoginLoadingDialog(){
        if (noticeDialog == null)
            noticeDialog = new Dialog(this);
        else if (noticeDialog.isShowing()) {
            noticeDialog.dismiss();
        }

        noticeDialog.setContentView(R.layout.dialog_progress);
        Objects.requireNonNull(noticeDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        noticeDialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        noticeDialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;
        noticeDialog.getWindow().getAttributes().windowAnimations = R.style.NoticeDialogAnimation;

        NewtonCradleLoading loading = noticeDialog.findViewById(R.id.loading);

        noticeDialog.show();
        loading.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== RequestCode.RC_GOOGLE.getCode()){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                userModel.signInWithGoogle(account).addOnSuccessListener(authResult -> {
                    if(authResult.getAdditionalUserInfo().isNewUser()){
                        newUser();
                    }
                    else{
                        showSuccessfulMessage(UserOperations.LOGIN_USER);
                        goToMainView();
                    }
                }).addOnCanceledListener(() -> showError(new OperationCanceledException())).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showError(e);
                    }
                });
            } catch (Exception e) {
                dismissProgressDialog();
            }
        }
        else if (requestCode==RequestCode.RC_UPDATE_INMEDIATE.getCode()){
            if(resultCode!= Activity.RESULT_OK){
                UpdateCheck updateCheck = new UpdateCheck();
                updateCheck.checkUpdateAvailability(this,this);
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

        TV_Forgot_Password = findViewById(R.id.TV_Forgot_Password);

        ET_Mail = findViewById(R.id.ET_Mail);
        ET_Password = findViewById(R.id.ET_Password);

        callbackManager = CallbackManager.Factory.create();

        //Google
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        userModel = new User();
        userMessage = new UserMessage();

        UpdateCheck updateCheck = new UpdateCheck();
        updateCheck.checkUpdateAvailability(this,this);
    }

    @Override
    public void initListeners(){
        //Iniciar Sesi??n
        BTN_Login.setOnClickListener(v -> {
            try{
                BTN_Login.startAnimation(press);
                showLoginLoadingDialog();
                if(Objects.requireNonNull(ET_Mail.getText()).toString().length()==0 || Objects.requireNonNull(ET_Password.getText()).toString().length()==0){
                    showError(new InputDataException(InputDataException.Code.EMPTY_FIELD));
                }else {
                    com.nordokod.scio.old.entity.User user = new com.nordokod.scio.old.entity.User();
                    user.setEmail(ET_Mail.getText().toString());
                    user.setPassword(ET_Password.getText().toString());
                    userModel.signInWithMail(user).addOnSuccessListener(authResult -> {
                        if (authResult.getAdditionalUserInfo().isNewUser()) {
                            newUser();
                        } else {
                            showSuccessfulMessage(UserOperations.LOGIN_USER);
                            if (!userModel.isEmailVerified()) goToVerifyMail();
                            else goToMainView();
                        }
                    }).addOnCanceledListener(() -> showError(new OperationCanceledException())).addOnFailureListener(e -> showError(e));
                }
            }catch(Exception e){
                showError(e);
            }
        });

        //Google
        BTN_Google.setOnClickListener(v -> {
            BTN_Google.startAnimation(press);
            showLoginLoadingDialog();
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RequestCode.RC_GOOGLE.getCode());
        });

        //Facebook
        BTN_FB.setOnClickListener(v -> {
            BTN_FB.startAnimation(press);
            showLoginLoadingDialog();
            LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email","public_profile"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    userModel.signInWithFacebook(loginResult.getAccessToken()).addOnSuccessListener(authResult -> {
                        if(authResult.getAdditionalUserInfo().isNewUser()){
                            newUser();
                        }else{
                            showSuccessfulMessage(UserOperations.LOGIN_USER);
                            goToMainView();
                        }
                    }).addOnCanceledListener(() -> showError(new OperationCanceledException())).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showError(e);
                        }
                    });
                }
                @Override
                public void onCancel() {
                    showError(new OperationCanceledException());
                }
                @Override
                public void onError(FacebookException error) {
                    showError(error);
                }
            });
        });

        //Registrarse
        BTN_Signup.setOnClickListener(v -> {
            BTN_Signup.startAnimation(press);
            goToSignUpView();
        });

        //Recuperar Contrase??a
        TV_Forgot_Password.setOnClickListener(v -> {
            //activity restore password
        });
    }

    @Override
    public void initAnimations(){
        press = AnimationUtils.loadAnimation(this, R.anim.press);
    }

    private void newUser(){
        User userModel = new User();
        com.nordokod.scio.old.entity.User userEntity = new com.nordokod.scio.old.entity.User();
        userEntity = userModel.getBasicUserInfo();
        userModel.createUserInformation(userEntity).addOnSuccessListener(aVoid -> {
            showSuccessfulMessage(UserOperations.SIGN_UP_USER);
            goToPermissionView();
        });
    }

    private void goToMainView(){
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        dismissProgressDialog();
        startActivity(mainIntent);
        finish();
    }

    private void goToSignUpView(){
        Intent signUpIntent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(signUpIntent);
        dismissProgressDialog();
    }

    private void goToPermissionView(){
        Intent intent = new Intent(getApplicationContext(),PermissionActivity.class);
        dismissProgressDialog();
        startActivity(intent);
        finish();
    }

    private void goToVerifyMail(){
        Intent intent = new Intent(getApplicationContext(),VerifyMailActivity.class);
        dismissProgressDialog();
        startActivity(intent);
        finish();
    }

    private void showError(Exception exception){
        userMessage.showErrorMessage(this, userMessage.categorizeException(exception));
        dismissProgressDialog();
    }

    private void showSuccessfulMessage(UserOperations userOperations){
        userMessage.showSuccessfulOperationMessage(this,userOperations);
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
