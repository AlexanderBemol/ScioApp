package com.nordokod.scio.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.widget.Button;
import android.widget.ImageButton;

import com.nordokod.scio.Controller.LoginController;
import com.nordokod.scio.R;

public class LoginActivity extends AppCompatActivity {
    // Botones de Inicio de Sesion y Registrarse
    AppCompatButton btnSesion, btnRegistrarse;
    // Botones de Facebook y Google
    AppCompatImageButton btnFacebook, btnGoogle;
    // TextoVies para recuperar la contraseña
    AppCompatTextView txtRecuperar;
    // EditText de Usuario y Contraseña
    AppCompatEditText editUsuario, editContraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponents();

    }

    private void initComponents(){
        btnSesion       = (AppCompatButton)         findViewById(R.id.btnSesion);
        btnRegistrarse  = (AppCompatButton)         findViewById(R.id.btnRegistrarse);

        btnFacebook     = (AppCompatImageButton)    findViewById(R.id.btnFacebook);
        btnGoogle       = (AppCompatImageButton)    findViewById(R.id.btnGoogle);

        txtRecuperar    = (AppCompatTextView)       findViewById(R.id.txtRecuperar);

        editUsuario     = (AppCompatEditText)       findViewById(R.id.editUsuario);
        editContraseña  = (AppCompatEditText)       findViewById(R.id.editContraseña);
    }

    public void errorUsuario(){

    }
}
