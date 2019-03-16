package com.nordokod.scio.Entidad;

import java.util.ArrayList;

public class Configuration {
    private int secondsBQ;
    private boolean appLocker;
    private ArrayList<String> lockedApps;

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
