package com.nordokod.scio.Controller;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.nordokod.scio.R;
import com.nordokod.scio.View.FirstConfigurationActivity;
import com.nordokod.scio.Entidad.Error;

import java.util.ArrayList;

public class FirstConfigurationController {
    private FirstConfigurationActivity firstConfigurationActivity;
    private Activity activity;
    private Context context;

    private ArrayList<String> packages;
    private static final int MAX_APPS = 3;

    public void configController(FirstConfigurationActivity firstConfigurationActivity, Activity activity, Context context) {
        this.firstConfigurationActivity = firstConfigurationActivity;
        this.activity = activity;
        this.context = context;
    }

    public boolean onStateChanged(String packageName, boolean isChecked) {
        if (packages == null)
            packages = new ArrayList<>();

        if (isChecked && packages.size() < MAX_APPS) {
            addApp(packageName);
            return true;
        } else if (!isChecked) {
            removeApp(packageName);
            return false;
        } else {
            firstConfigurationActivity.showErrorNoticeDialog(new Error(Error.MAXIMUM_NUMBER_OF_APPS_REACHED));
            return false;
        }
    }

    private void removeApp(String packageName) {
        packages.remove(packageName);
    }

    private void addApp(String packageName) {
        packages.add(packageName);
    }

    public void updateAppBlocked() {
        firstConfigurationActivity.showSuccessNoticeDialog(null);
    }

}
