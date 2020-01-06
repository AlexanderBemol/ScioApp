package com.nordokod.scio.view;

import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import android.view.View;

import com.nordokod.scio.R;
import com.nordokod.scio.process.PermissionCheck;

public class PermissionActivity extends AppCompatActivity implements BasicActivity, LifecycleObserver {
    /**
     * Objeto del botón usado para activar los permisos.
     */
    private AppCompatButton BTN_Overlay;
    private AppCompatButton BTN_Usage;
    private AppCompatButton BTN_Autostart;
    private AppCompatButton BTN_Skip;

    private PermissionCheck permissionCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        initComponents();
        initListeners();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        checkPermissionState();
    }
    @Override
    protected void onResume() {
        super.onResume();
        checkPermissionState();
    }
    @Override
    public void initComponents() {
        BTN_Overlay = findViewById(R.id.BTN_Overlay_permission);
        BTN_Usage = findViewById(R.id.BTN_Usage_access_permission);
        BTN_Autostart = findViewById(R.id.BTN_Autostart_permission);
        BTN_Skip = findViewById(R.id.BTN_Permission_skip);

        permissionCheck = new PermissionCheck();

        if(!permissionCheck.haveAutostart())BTN_Autostart.setVisibility(View.INVISIBLE);

        checkPermissionState();
    }

    @Override
    public void initListeners() {
        BTN_Usage.setOnClickListener((view)->permissionCheck.askForPermission(PermissionCheck.PermissionEnum.USAGE_STATS,this));
        BTN_Overlay.setOnClickListener(view -> permissionCheck.askForPermission(PermissionCheck.PermissionEnum.SYSTEM_ALERT_WINDOW,this));
        BTN_Autostart.setOnClickListener(view -> permissionCheck.askForPermission(PermissionCheck.PermissionEnum.AUTO_START,this));
        BTN_Skip.setOnClickListener(view->goToMain());
    }





    private void checkPermissionState(){
        if(permissionCheck.permissionIsGranted(PermissionCheck.PermissionEnum.SYSTEM_ALERT_WINDOW,this,this))BTN_Overlay.setSupportBackgroundTintList(ContextCompat.getColorStateList(PermissionActivity.this, R.color.correctColor));
        else BTN_Overlay.setSupportBackgroundTintList(ContextCompat.getColorStateList(PermissionActivity.this, R.color.errorColor));
        if(permissionCheck.permissionIsGranted(PermissionCheck.PermissionEnum.USAGE_STATS,this,this))BTN_Usage.setSupportBackgroundTintList(ContextCompat.getColorStateList(PermissionActivity.this, R.color.correctColor));
        else BTN_Usage.setSupportBackgroundTintList(ContextCompat.getColorStateList(PermissionActivity.this, R.color.errorColor));
    }

    private void goToMain(){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }



}
