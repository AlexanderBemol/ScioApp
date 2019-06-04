package com.nordokod.scio.controller;

import android.app.Activity;
import android.content.Context;

import com.nordokod.scio.entity.App;
import com.nordokod.scio.entity.Error;
import com.nordokod.scio.model.AppBlockedModel;
import com.nordokod.scio.view.AppBlockedActivity;

import java.util.ArrayList;

public class AppBlockedController {
    private Context currentContext;
    private AppBlockedActivity abActivity;
    private Activity currentActivity;
    private static AppBlockedModel abModel;

    public  AppBlockedController(Context context, Activity activity, AppBlockedActivity aba){
        this.currentActivity=activity;
        this.currentContext=context;
        this.abActivity=aba;
        this.abModel=new AppBlockedModel(this,context);
    }
    public void showError(Error error) {
        abActivity.showErrorNoticeDialog(error);
    }
    public static ArrayList<App> getListOfApps() {
        return abModel.getListOfApps();
    }
}
