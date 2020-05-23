package com.nordokod.scio.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;

import com.nordokod.scio.R;
import com.nordokod.scio.constants.UserOperations;
import com.nordokod.scio.model.User;
import com.nordokod.scio.process.UserMessage;

public class VerifyMailActivity extends AppCompatActivity implements BasicActivity{

    private User userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: Agregar la logica para el tema oscuro y claro
        setContentView(R.layout.activity_verify_mail);
        initComponents();
        initListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateState();
    }

    @Override
    public void initComponents() {
        BTN_Resend  = findViewById(R.id.VMail_BTN_Resend);
        BTN_Logout  = findViewById(R.id.VMail_BTN_Logout);

        Swipe       = findViewById(R.id.VMail_Swipe);
    }

    @Override
    public void initListeners() {
        BTN_Logout.setOnClickListener(v -> {
            userModel.logOut();
            goToLoginActivity();
        });

        BTN_Resend.setOnClickListener(v -> userModel.sendVerificationMail()
                .addOnSuccessListener(a -> showMessage(UserOperations.RESEND_VERIFICATION_MAIL))
                .addOnFailureListener(this::showError));

        Swipe.setOnRefreshListener(this::updateState);
    }

    /**
     * Método para confirmar si el usuario ya verificó su cuenta.
     */
    private void updateState(){
        if(userModel == null) userModel = new User();

        userModel.refreshUser()
                .addOnSuccessListener(aVoid ->{
                    if(userModel.isEmailVerified()){
                        showMessage(UserOperations.MAIL_VERIFICATED);
                        goToMainActivity();
                    }
                    if(Swipe!=null)Swipe.setRefreshing(false);
                })
                .addOnCanceledListener(()->{
                    if(Swipe!=null)Swipe.setRefreshing(false);
                })
                .addOnFailureListener(e -> {
                    showError(e);
                    if(Swipe!=null)Swipe.setRefreshing(false);
        });

    }

    /**
     * Método que inicializa y abre MainActvity y cierra el actual Activity.
     */
    private void goToMainActivity() {
        Intent intent= new Intent(this, MainActivity.class);
        this.startActivity(intent);
        finish();
    }

    /**
     * Método que inicializa y abre LoginActvity y cierra el actual Activity.
     */
    private void goToLoginActivity() {
        Intent intent= new Intent(this, LoginActivity.class);
        this.startActivity(intent);
        finish();
    }

    private void showError(Exception e) {
        UserMessage userMessage = new UserMessage();
        userMessage.showErrorMessage(getApplicationContext(), userMessage.categorizeException(e));
    }

    private void showMessage(UserOperations userOperation) {
        UserMessage userMessage = new UserMessage();
        userMessage.showSuccessfulOperationMessage(getApplicationContext(), userOperation);
    }

    ////////////////////////////////////////////////////////////////////////// Objects from the view
    private AppCompatButton BTN_Resend;
    private AppCompatTextView BTN_Logout;
    private SwipeRefreshLayout Swipe;
}