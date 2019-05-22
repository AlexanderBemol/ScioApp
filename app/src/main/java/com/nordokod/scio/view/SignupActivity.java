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

public class SignupActivity extends AppCompatActivity implements BasicActivity{
    private static final int NOTICE_DIALOG_TIME = 2000;
    private AppCompatButton BTN_Cancel,BTN_Signup;

    private AppCompatEditText ET_Mail,ET_Password,ET_ConfirmPassword;

    private Dialog RegisterDialog, noticeDialog;

    private Animation press;

    private SignUpController SignupController;


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
        BTN_Cancel = findViewById(R.id.BTN_Cancel);
        BTN_Signup = findViewById(R.id.BTN_Signup);

        ET_Mail = findViewById(R.id.ET_Mail);
        ET_Password = findViewById(R.id.ET_Password);
        ET_ConfirmPassword = findViewById(R.id.ET_ConfirmPassword);

        SignupController = new SignUpController(this,this,this);


    }

    @Override
    public void initListeners() {
        BTN_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BTN_Signup.startAnimation(press);
                showLoadingDialog();
                SignupController.signUpUser(ET_Mail.getText().toString(),ET_Password.getText().toString(),ET_ConfirmPassword.getText().toString());
            }
        });
        BTN_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BTN_Cancel.startAnimation(press);
                onBackPressed();
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

    private void showLoadingDialog(){
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
}
