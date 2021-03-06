package com.nordokod.scio.old.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.nordokod.scio.R;
import com.nordokod.scio.old.constants.UserOperations;
import com.nordokod.scio.old.entity.App;
import com.nordokod.scio.old.entity.ConfigurationApp;
import com.nordokod.scio.old.process.LockAppProcess;
import com.nordokod.scio.old.process.SystemWriteProcess;
import com.nordokod.scio.old.process.UserMessage;

import java.util.ArrayList;
import java.util.Objects;

public class LockedAppsOptionActivity extends AppCompatActivity implements BasicActivity{

    private RecyclerView listApps;
    private RecyclerView.Adapter mAdapter;
    //private SwitchCompat switchCompat;
    private AppCompatButton BTN_Save;
    private AppCompatButton BTN_Cancel;
    private SystemWriteProcess systemWriteProcess;

    private ConfigurationApp configurationApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locked_apps_option);
        initComponents();
        initListeners();
    }

    @Override
    public void initComponents() {
        listApps = findViewById(R.id.AppBlocked_RV_listApps);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        listApps.setLayoutManager(layoutManager);
        systemWriteProcess = new SystemWriteProcess(this);

        ArrayList<App> appArrayList = systemWriteProcess.getAllApps();

        BTN_Save = findViewById(R.id.AppBlocked_BTN_Save);
        BTN_Cancel = findViewById(R.id.AppBlocked_BTN_Cancel);

        configurationApp = systemWriteProcess.readUserConfig();

        listApps.setActivated(configurationApp.isAppLocker());

        for (App app : appArrayList){
            if(configurationApp.getLockedApps().contains(app.getPackagePath())){
                app.setState(true);
            }
        }
        mAdapter = new AppRecyclerViewAdapter(appArrayList);
        listApps.setAdapter(mAdapter);
    }

    @Override
    public void initListeners() {
        BTN_Save.setOnClickListener(l->{
            ArrayList<String> lockedApps = new ArrayList<>();
            for (App app :((AppRecyclerViewAdapter)mAdapter).getAppArrayList()){
                if(app.isState())lockedApps.add(app.getPackagePath());
            }
            configurationApp.setLockedApps(lockedApps);
            systemWriteProcess.saveUserConfig(configurationApp);
            Intent service= new Intent(this, LockAppProcess.class);

            if(configurationApp.isAppLocker()){
                if(isMyServiceRunning()){
                    stopService(service);
                    startService(service);
                }else
                    startService(service);
            }else
                stopService(service);

            UserMessage userMessage = new UserMessage();
            userMessage.showSuccessfulOperationMessage(this, UserOperations.SAVE_CONFIGURATION);
            closeAndGoToMainActivity();
        });
        BTN_Cancel.setOnClickListener(l->closeAndGoToMainActivity());
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : Objects.requireNonNull(manager).getRunningServices(Integer.MAX_VALUE)) {
            if (LockAppProcess.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void closeAndGoToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
