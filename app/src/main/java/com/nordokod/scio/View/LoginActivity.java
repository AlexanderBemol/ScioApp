package com.nordokod.scio.View;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.nordokod.scio.Controller.LoginController;
import com.nordokod.scio.R;

public class LoginActivity extends AppCompatActivity {
    // Botones de Inicio de Sesion y Registrarse
    AppCompatButton btnSesion, btnRegistrarse;
    // Botones de Facebook y Google
    AppCompatImageButton  btnGoogle,btnFB;
    LoginButton btnFacebook;
    // TextoVies para recuperar la contraseña
    AppCompatTextView txtRecuperar;
    // EditText de User y Contraseña
    AppCompatEditText editUsuario, editContraseña;
    LoginController logController;
    CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponents();
        if (!isAccessGranted()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }

    }

    private void initComponents(){
        btnSesion       = (AppCompatButton)         findViewById(R.id.btnSesion);
        btnRegistrarse  = (AppCompatButton)         findViewById(R.id.btnRegistrarse);

        btnFB     =  findViewById(R.id.btnFacebook);
        btnGoogle       = (AppCompatImageButton)    findViewById(R.id.btnGoogle);

        txtRecuperar    = (AppCompatTextView)       findViewById(R.id.txtRecuperar);

        editUsuario     = (AppCompatEditText)       findViewById(R.id.editUsuario);
        editContraseña  = (AppCompatEditText)       findViewById(R.id.editContraseña);

        btnFacebook     = new LoginButton(this);

        mCallbackManager= CallbackManager.Factory.create();

        logController   =   new LoginController();//crear instancia
        logController.configController(this,this,this);//pasar context, actual activity y LoginActivity
        logController.initializeFirebase();//inicializar firebase

        logController.confGoogle();

        btnFacebook.setReadPermissions("email", "public_profile");
        logController.confFB(mCallbackManager,btnFacebook);

        btnSesion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                logController.signinWithMail(editUsuario.getText().toString(),editContraseña.getText().toString());//intentar iniciar sesión
            }
        });
        btnGoogle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                logController.signinGoogle();
            }
        });
        btnFB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                btnFacebook.callOnClick();
            }
        });

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,AppsList.class);
                startActivity(i);
            }
        });
    }

    public void errorUsuario(String error){
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
    }
    public void correctoUsuario(){
        Toast.makeText(this,"Inicio de sesión correcto",Toast.LENGTH_SHORT).show();
    }
    public void camposIncompletos(){
        Toast.makeText(this,"Ningún campo puede estar vacío",Toast.LENGTH_SHORT).show();
    }

    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        logController.onResult(requestCode,resultCode,data);
    }
}
