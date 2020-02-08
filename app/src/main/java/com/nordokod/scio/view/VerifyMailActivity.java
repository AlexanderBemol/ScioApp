package com.nordokod.scio.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.nordokod.scio.R;
import com.nordokod.scio.constants.UserOperations;
import com.nordokod.scio.model.User;
import com.nordokod.scio.process.UserMessage;

public class VerifyMailActivity  extends AppCompatActivity implements BasicActivity {

    private AppCompatButton BTN_Refresh, BTN_Resend, BTN_Logout;
    private User userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_verify_mail);
        initComponents();
        initListeners();
    }

    @Override
    public void initComponents() {
        BTN_Refresh = findViewById(R.id.BTN_Verify_Mail_Refresh);
        BTN_Resend = findViewById(R.id.BTN_Verify_Mail_Resend);
        BTN_Logout = findViewById(R.id.BTN_Verify_Mail_Logout);
    }

    @Override
    public void initListeners() {
        BTN_Refresh.setOnClickListener(l -> {
            updateState();
        });
        BTN_Resend.setOnClickListener(l ->
            userModel.sendVerificationMail()
                    .addOnSuccessListener(a->showMessage())
                    .addOnFailureListener(this::showError)
        );
        BTN_Logout.setOnClickListener(l -> {
            userModel.logOut();
            goToLogin();
        });
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        updateState();
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateState();
    }

    private void updateState(){
        if(userModel==null)userModel=new User();
        userModel.refreshUser()
                .addOnSuccessListener(aVoid ->{
                    if(userModel.isEmailVerified()){
                        goToMain();
                    }
                })
                .addOnFailureListener(this::showError);
    }

    private void goToMain(){
        Intent intent= new Intent(this, MainActivity.class);
        this.startActivity(intent);
        finish();
    }

    private void goToLogin(){
        Intent intent= new Intent(this, LoginActivity.class);
        this.startActivity(intent);
        finish();
    }
    private void showError(Exception e){
        UserMessage userMessage = new UserMessage();
        userMessage.showErrorMessage(getApplicationContext(),userMessage.categorizeException(e));
    }
    private void showMessage(){
        UserMessage userMessage = new UserMessage();
        userMessage.showSuccessfulOperationMessage(getApplicationContext(), UserOperations.CREATE_USER);
    }
}
