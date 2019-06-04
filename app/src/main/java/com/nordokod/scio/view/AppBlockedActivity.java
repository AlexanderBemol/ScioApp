package com.nordokod.scio.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;

import com.nordokod.scio.R;
import com.nordokod.scio.controller.AppBlockedController;
import com.nordokod.scio.entity.App;
import com.nordokod.scio.entity.Error;

import java.util.ArrayList;

public class AppBlockedActivity extends AppCompatActivity implements BasicActivity {

    private RecyclerView listApps;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<App> arraylistapp;
    private SwitchCompat switchCompat;
    private AppCompatButton BTN_Save;
    private AppCompatButton BTN_Cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_blocked);
    }

    @Override
    public void initComponents() {
    switchCompat = findViewById(R.id.AppBlocked_SC_Activated);
    listApps = findViewById(R.id.AppBlocked_RV_listApps);

    layoutManager = new LinearLayoutManager(getApplicationContext());
    listApps.setLayoutManager(layoutManager);

    mAdapter = new AppRecyclerViewAdapter(getListOfApps());

    BTN_Save = findViewById(R.id.AppBlocked_BTN_Save);
    BTN_Cancel = findViewById(R.id.AppBlocked_BTN_Cancel);
    }
    private ArrayList<App> getListOfApps() {
        return AppBlockedController.getListOfApps();
    }


    @Override
    public void initListeners() {

    }

    @Override
    public void showErrorNoticeDialog(Error error) {

    }

    @Override
    public void showSuccessNoticeDialog(String task) {

    }
}
