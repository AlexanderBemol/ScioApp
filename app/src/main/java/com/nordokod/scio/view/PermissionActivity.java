package com.nordokod.scio.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.nordokod.scio.entity.Error;
import com.nordokod.scio.R;

public class PermissionActivity extends AppCompatActivity implements BasicActivity {
    /**
     * Objeto del botón usado para activar los permisos.
     */
    private AppCompatButton BTN_Activate;

    /**
     * Objeto del controlador perteneciente a este módulo.
     */
    //private PermissionController permissionController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        initComponents();
        initListeners();
    }

    @Override
    public void initComponents() {
        BTN_Activate = findViewById(R.id.BTN_Activate);

        //permissionController = new PermissionController();

        changeStateBtnActivate(checkPermissions());
    }

    @Override
    public void initListeners() {
        BTN_Activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activatePermissions();
            }
        });
    }

    @Override
    public void showErrorNoticeDialog(Error error) {
        changeStateBtnActivate(checkPermissions());
    }

    @Override
    public void showSuccessNoticeDialog(String task) {
        changeStateBtnActivate(checkPermissions());
        
        finishActivity();
    }

    /**
     * Método usado para validar que los permisos estén activos.
     *
     * @return  true    = Todos los permisos están activados.
     *          false   = Hay al menos un permiso desactivado.
     */
    private boolean checkPermissions() {
        //return permissionController.checkPermissions();
        return true;
    }

    /**
     * Método usado para cambiar el color del botón.
     *
     * @param isActivated   true    = Todos los permisos están activados.
     *                      false   = Hay al menos un permiso desactivado.
     */
    @SuppressLint({"ResourceAsColor", "RestrictedApi"})
    private void changeStateBtnActivate(boolean isActivated) {
        if (isActivated)
            BTN_Activate.setSupportBackgroundTintList(ContextCompat.getColorStateList(PermissionActivity.this, R.color.correctColor));
        else
            BTN_Activate.setSupportBackgroundTintList(ContextCompat.getColorStateList(PermissionActivity.this, R.color.errorColor));
    }

    /**
     * Método usado para mandar a activar los permisos.
     */
    private void activatePermissions() {
        //permissionController.activatePermissions();
    }

    private void finishActivity() {
        if ("First Configuration".equals(getIntent().getAction())) {
            Intent intent = new Intent(PermissionActivity.this, FirstConfigurationActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
