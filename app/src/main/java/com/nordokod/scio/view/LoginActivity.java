package com.nordokod.scio.view;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.nordokod.scio.controller.LoginController;
import com.nordokod.scio.entity.Error;
import com.nordokod.scio.R;
import com.victor.loading.newton.NewtonCradleLoading;

import java.util.Objects;

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
    //Controller
    private LoginController loginController;
    private CallbackManager mCallbackManager;
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

        if(loginController.IsUserLogged()){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginController.onResult(requestCode,resultCode,data);
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

        BTN_Facebook = new LoginButton(this);

        mCallbackManager= CallbackManager.Factory.create();

        loginController = new LoginController();//crear instancia
        loginController.configController(this,this,this);//pasar context, actual activity y LoginActivity
        loginController.initializeFirebase();//inicializar firebase

        loginController.confGoogle();

        BTN_Facebook.setReadPermissions("email", "public_profile");
        loginController.confFB(mCallbackManager, BTN_Facebook);


    }

    @Override
    public void initListeners(){
        //Iniciar Sesión
        BTN_Login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                BTN_Login.startAnimation(press);
                showLoginLoadingDialog();
                loginController.loginWithMail(ET_Mail.getText().toString(), ET_Password.getText().toString());
            }
        });

        //Google
        BTN_Google.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showLoginLoadingDialog();
                loginController.loginGoogle();
            }
        });

        //Facebook
        BTN_FB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showLoginLoadingDialog();
                BTN_Facebook.callOnClick();
            }
        });

        //Registrarse
        BTN_Signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                BTN_Signup.startAnimation(press);
                // Aquí debe ir la llamada al SigninActivity.
                    Intent i = new Intent(LoginActivity.this,SignupActivity.class);
                    startActivity(i);

            }
        });

        //Recuperar Contraseña
        txtRecuperar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // No sé cómo vayamos a hacer esta opción.
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

        Intent intent = new Intent(LoginActivity.this, FirstConfigurationActivity.class);
        intent.setAction("First Configuration");
        startActivity(intent);
    }

    private void initAnimations(){
        press = AnimationUtils.loadAnimation(this, R.anim.press);
    }
}
