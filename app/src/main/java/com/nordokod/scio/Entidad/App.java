package com.nordokod.scio.Entidad;

import android.graphics.drawable.Drawable;

public class App {
    private Drawable icon;
    private String name, packagePath;
    private boolean state;

    public App(Drawable icon, String name, String packagePath, boolean state) {
        this.icon = icon;
        this.name = name;
        this.packagePath = packagePath;
        this.state = state;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
