package com.nordokod.scio.model;

import android.app.Activity;
import android.content.Context;

import com.nordokod.scio.controller.AppBlockedController;
import com.nordokod.scio.controller.FirstConfigurationController;
import com.nordokod.scio.entity.App;
import com.nordokod.scio.entity.Error;
import com.nordokod.scio.process.SystemWriteProcess;

import java.util.ArrayList;

public class AppBlockedModel {
    private Context currentContext;
    private int lockedApps;
    private AppBlockedController abController;


    public AppBlockedModel(AppBlockedController abc, Context context) {
        this.currentContext = context;
        this.abController = abc;
        lockedApps = 0;
    }

    public ArrayList<App> getListOfApps() {
        SystemWriteProcess swp=new SystemWriteProcess(currentContext);
        return swp.getAllApps();
    }

    public boolean onStateChanged(String packagePath, boolean isChecked) {
        boolean b;
        if(!isChecked){
            lockedApps--;
            b=false;
        }else{
            if(lockedApps<3){
                lockedApps++;
                b=true;
            }else{
                b=false;
                Error error=new Error();
                error.setType(Error.MAXIMUM_NUMBER_OF_APPS_REACHED);
                abController.showError(error);
            }
        }
        return b;
    }
}
