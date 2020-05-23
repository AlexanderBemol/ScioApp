package com.nordokod.scio.view;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nordokod.scio.R;
import com.nordokod.scio.constants.UserOperations;
import com.nordokod.scio.entity.InputDataException;
import com.nordokod.scio.entity.OperationCanceledException;
import com.nordokod.scio.model.User;
import com.nordokod.scio.process.UserMessage;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity implements BasicActivity{
    private static final int NOTICE_DIALOG_TIME = 2000;

    private AppCompatButton BTN_Cancel,BTN_Signup;
    private AppCompatEditText ET_Mail,ET_Password,ET_ConfirmPassword;
    private Animation press;
    private ProgressDialog progressDialog;
    private User userModel;

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
        userModel = new User();

    }

    @Override
    public void initListeners() {
        BTN_Signup.setOnClickListener(view -> {
            BTN_Signup.startAnimation(press);
            if (!Objects.requireNonNull(ET_Mail.getText()).toString().equals("") && !Objects.requireNonNull(ET_Password.getText()).toString().equals("") && !Objects.requireNonNull(ET_ConfirmPassword.getText()).toString().equals("")){
                if(ET_Password.length() >= 8){
                    if(ET_Password.getText().toString().equals(ET_ConfirmPassword.getText().toString())){
                        onShowProgressDialog();
                        com.nordokod.scio.entity.User user = new com.nordokod.scio.entity.User();
                        user.setEmail(Objects.requireNonNull(ET_Mail.getText()).toString());
                        user.setPassword(ET_Password.getText().toString());
                        userModel.signUpWithMail(user)
                                .addOnSuccessListener(authResult -> onSuccessSignUp())
                                .addOnCanceledListener(() -> showError(new OperationCanceledException()))
                                .addOnFailureListener(this::showError);

                    }else showError(new InputDataException(InputDataException.Code.PASSWORDS_DONT_MATCH));
                }else showError(new InputDataException(InputDataException.Code.INVALID_PASSWORD));
            }else showError(new InputDataException(InputDataException.Code.EMPTY_FIELD));
        });

        BTN_Cancel.setOnClickListener(view -> {
            BTN_Cancel.startAnimation(press);
            onBackPressed();
        });

        // TODO: Cuando tengamos hechos los terminos y condiciones, y el aviso de privacidad
        //  debemos agregar que con un clic lo redireccione a leerlos.
    }

    public void onSuccessSignUp() {
        if(progressDialog.isShowing())
            progressDialog.dismiss();

        com.nordokod.scio.entity.User user = new com.nordokod.scio.entity.User();
        user.setEmail(Objects.requireNonNull(ET_Mail.getText()).toString());
        user.setPassword(Objects.requireNonNull(ET_Password.getText()).toString());
        user.setUsername(user.getEmail());
        userModel.sendVerificationMail()
                .addOnSuccessListener(aVoid ->
                    userModel.createUserInformation(user)
                            .addOnSuccessListener(aVoid1 -> showMessage())
                            .addOnFailureListener(this::showError)
                )
                .addOnFailureListener(this::showError);
        Intent intent= new Intent(this, VerifyMailActivity.class);
        this.startActivity(intent);
        finish();
    }


    public void onShowProgressDialog() {
        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.message_signup_loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void initAnimations(){
        press = AnimationUtils.loadAnimation(this, R.anim.press);
    }

    private void showError(Exception e){
        if(progressDialog != null)if(progressDialog.isShowing()) progressDialog.dismiss();
        UserMessage userMessage = new UserMessage();
        userMessage.showErrorMessage(this,userMessage.categorizeException(e));
    }
    private void showMessage(){
        UserMessage userMessage = new UserMessage();
        userMessage.showSuccessfulOperationMessage(getApplicationContext(), UserOperations.SIGN_UP_USER);
    }
}
