package com.nordokod.scio.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nordokod.scio.R;
import com.nordokod.scio.controller.SignUpController;
import com.nordokod.scio.entity.Error;
import com.victor.loading.newton.NewtonCradleLoading;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class SignupActivity extends AppCompatActivity implements BasicActivity{
    private static final int NOTICE_DIALOG_TIME = 2000;

    private AppCompatButton BTN_Cancel,BTN_Signup;
    private AppCompatEditText ET_Mail,ET_Password,ET_ConfirmPassword;
    private Dialog noticeDialog;
    private Animation press;
    private SignUpController SignupController;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initComponents();
        initAnimations();
        initListeners();
    }


    @Override
    public void initComponents() {
        BTN_Cancel          = findViewById(R.id.SU_BTN_Cancel);
        BTN_Signup          = findViewById(R.id.SU_BTN_Signup);

        ET_Mail             = findViewById(R.id.SU_ET_Mail);
        ET_Password         = findViewById(R.id.SU_ET_Password);
        ET_ConfirmPassword  = findViewById(R.id.SU_ET_ConfirmPassword);

        SignupController = new SignUpController(this,SignupActivity.this);
    }

    @Override
    public void initListeners() {
        BTN_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BTN_Signup.startAnimation(press);

                SignupController.signUpUser(
                        Objects.requireNonNull(ET_Mail.getText()).toString(),
                        Objects.requireNonNull(ET_Password.getText()).toString(),
                        Objects.requireNonNull(ET_ConfirmPassword.getText()).toString());
            }
        });

        BTN_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BTN_Cancel.startAnimation(press);
                onBackPressed();
            }
        });

        // TODO: Cuando tengamos hechos los terminos y condiciones, y el aviso de privacidad
        //  debemos agregar que con un clic lo redireccione a leerlos.
    }

    public void onSuccessSignUp() {
        if(progressDialog.isShowing())
            progressDialog.dismiss();

        Toasty.success(this, R.string.message_login_success).show();

        Intent intent= new Intent(this, FirstConfigurationActivity.class);
        this.startActivity(intent);
        finish();
    }

    public void onEmptyFields() {
        Toasty.warning(this, R.string.message_emptyfields_error).show();
    }

    public void onErrorSignUp() {
        Toasty.error(this, R.string.message_error).show();
    }

    public void onShowProgressDialog() {
        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.message_signup_loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void onInvalidPassword() {
        Toasty.error(this, R.string.message_invalid_password).show();
    }

    public void onErrorMatchPasswords() {
        Toasty.error(this, R.string.message_match_password_error).show();
    }

    public void onRejectedEmail() {
        Toasty.error(this, R.string.message_rejected_email).show();
    }

    private void initAnimations(){
        press = AnimationUtils.loadAnimation(this, R.anim.press);
    }
}
