package com.nordokod.scio.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nordokod.scio.R;
import com.nordokod.scio.controller.SignUpController;
import com.nordokod.scio.entity.Error;
import com.victor.loading.newton.NewtonCradleLoading;

public class SignupActivity extends AppCompatActivity implements BasicActivity{
    private AppCompatButton BTN_Cancel,BTN_Signup;

    private AppCompatEditText ET_Mail,ET_Password,ET_ConfirmPassword;

    private Dialog RegisterDialog, noticeDialog;

    private Animation press;

    private SignUpController SignupController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }


    @Override
    public void initComponents() {
        BTN_Cancel = findViewById(R.id.BTN_Cancel);
        BTN_Signup = findViewById(R.id.BTN_Signup);

        ET_Mail = findViewById(R.id.ET_Mail);
        ET_Password = findViewById(R.id.ET_Password);
        ET_ConfirmPassword = findViewById(R.id.ET_ConfirmPassword);

        SignupController = new SignUpController();
        SignupController.signUpUser(ET_Mail.toString(),ET_Password.toString(),ET_ConfirmPassword.toString());

    }

    @Override
    public void initListeners() {
        BTN_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BTN_Signup.startAnimation(press);
                showLoginDialog();

            }
        });
    }

    @Override
    public void showErrorNoticeDialog(Error error) {

    }

    @Override
    public void showSuccessNoticeDialog(String task) {

    }
    private void initAnimations(){
        press = AnimationUtils.loadAnimation(this, R.anim.press);
    }
    public void showLoginDialog(){
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
