package com.nordokod.scio.Entidad;

import java.io.Serializable;
import java.util.ArrayList;

public class ConfigurationApp implements Serializable {
    private int secondsBQ;
    private boolean appLocker;
    private ArrayList<String> lockedApps;

    public ArrayList<appData> getAllApps() {
        return allApps;
    }
    public void setAllApps(ArrayList<appData> allApps) {
        this.allApps = allApps;
    }
    private ArrayList<appData> allApps;

    public int getSecondsBQ() {
        return secondsBQ;
    }

    public void setSecondsBQ(int secondsBQ) {
        this.secondsBQ = secondsBQ;
    }

    public boolean isAppLocker() {
        return appLocker;
    }

    public void setAppLocker(boolean appLocker) {
        this.appLocker = appLocker;
    }

    public ArrayList<String> getLockedApps() {
        return lockedApps;
    }

    public void setLockedApps(ArrayList<String> lockedApps) {
        this.lockedApps = lockedApps;
    }


}
