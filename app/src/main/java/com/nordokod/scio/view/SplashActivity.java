package com.nordokod.scio.view;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.nordokod.scio.R;
import com.nordokod.scio.kt.ui.login.LoginView;
import com.nordokod.scio.process.LockAppProcess;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent service= new Intent(this,LockAppProcess.class);
        if(!isMyServiceRunning(LockAppProcess.class)){
            startService(service);
        }

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginView.class);
            startActivity(intent);
            finish();
        }, 1500);
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
