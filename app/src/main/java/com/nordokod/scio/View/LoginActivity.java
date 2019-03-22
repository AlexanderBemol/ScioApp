package com.nordokod.scio.View;

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
import com.nordokod.scio.Controller.LoginController;
import com.nordokod.scio.R;
import com.victor.loading.newton.NewtonCradleLoading;

public class LoginActivity extends AppCompatActivity {
    // Botones de Inicio de Sesion y Registrarse
    private AppCompatButton btnSesion, btnRegistrarse;
    // Facebook and Google Buttons
    private AppCompatImageButton btnGoogle, btnFB;
    private LoginButton btnFacebook;
    // Recover password TextView
    private AppCompatTextView txtRecuperar;
    // User and Password EditTexts
    private AppCompatEditText editUsuario, editContraseña;
    //Controller
    private LoginController logController;
    private CallbackManager mCallbackManager;
    // Animations
    private Animation press;
    // Dialogs
    private Dialog loginErrorDialog, noticeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponents();
        initAnimations();
        initListeners();
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

    public void showLoginSuccessDialog(){
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

        AppCompatTextView message = noticeDialog.findViewById(R.id.txtMessage);
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

    public void showEmptyFieldsDialog(){
        if (noticeDialog == null)
            noticeDialog = new Dialog(this);
        else if (noticeDialog.isShowing()) {
            noticeDialog.dismiss();
        }

        noticeDialog.setContentView(R.layout.dialog_error);
        noticeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        noticeDialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        noticeDialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;
        noticeDialog.getWindow().getAttributes().windowAnimations = R.style.NoticeDialogAnimation;

        AppCompatTextView message = noticeDialog.findViewById(R.id.txtMessage);
        message.setText(R.string.message_emptyfields_error);

        noticeDialog.show();

        Handler handler;
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                noticeDialog.cancel();
                noticeDialog.dismiss();
                noticeDialog = null;
            }
        }, 2000);

        handler = null;
    }

    public void showErrorDialog(final String error){
        loginErrorDialog = new Dialog(this);
        loginErrorDialog.setContentView(R.layout.dialog_error_login);
        loginErrorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        AppCompatImageView  image   = loginErrorDialog.findViewById(R.id.imageError);
        AppCompatTextView   text    = loginErrorDialog.findViewById(R.id.textError);

        switch (error){
            case "MAIL":
                image.setImageResource(R.drawable.mail_error);
                text.setText(R.string.error_mail);
                break;
            case "FACEBOOK":
                image.setImageResource(R.drawable.facebook_error);
                text.setText(R.string.error_facebook);
                break;
            case "GOOGLE":
                image.setImageResource(R.drawable.google_error);
                text.setText(R.string.error_google);
                break;
        }

        loginErrorDialog.show();

        if (noticeDialog != null && noticeDialog.isShowing()) {
            noticeDialog.cancel();
            noticeDialog.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        logController.onResult(requestCode,resultCode,data);
    }

    private void initComponents(){
        btnSesion       = findViewById(R.id.btnSesion);
        btnRegistrarse  = findViewById(R.id.btnRegistrarse);

        btnFB           = findViewById(R.id.btnFacebook);
        btnGoogle       = findViewById(R.id.btnGoogle);

        txtRecuperar    = findViewById(R.id.txtRecuperar);

        editUsuario     = findViewById(R.id.editUsuario);
        editContraseña  = findViewById(R.id.editContraseña);

        btnFacebook     = new LoginButton(this);

        mCallbackManager= CallbackManager.Factory.create();

        logController   = new LoginController();//crear instancia
        logController.configController(this,this,this);//pasar context, actual activity y LoginActivity
        logController.initializeFirebase();//inicializar firebase

        logController.confGoogle();

        btnFacebook.setReadPermissions("email", "public_profile");
        logController.confFB(mCallbackManager,btnFacebook);


    }

    private void initListeners(){
        //Iniciar Sesión
        btnSesion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                btnSesion.startAnimation(press);
                showLoginLoadingDialog();
                logController.loginWithMail(editUsuario.getText().toString(), editContraseña.getText().toString());
            }
        });

        //Google
        btnGoogle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showLoginLoadingDialog();
                logController.loginGoogle();
            }
        });

        //Facebook
        btnFB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                btnFacebook.callOnClick();
            }
        });

        //Registrarse
        btnRegistrarse.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                btnRegistrarse.startAnimation(press);
                // Aquí debe ir la llamada al SigninActivity.
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

    private void initAnimations(){
        press = AnimationUtils.loadAnimation(this, R.anim.press);
    }
}
