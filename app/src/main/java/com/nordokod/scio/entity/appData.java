package com.nordokod.scio.entity;

import android.graphics.drawable.Drawable;

public class appData {
    private String appName,appPackage;
    private  boolean appState;
    private Drawable icon;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public boolean isAppState() {
        return appState;
    }

    public void setAppState(boolean appState) {
        this.appState = appState;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }


}
