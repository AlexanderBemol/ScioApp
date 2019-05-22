package com.nordokod.scio.process;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.nordokod.scio.entity.App;
import com.nordokod.scio.entity.ConfigurationApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class SystemWriteProcess {
    private File internalStorageDir;
    private Context currentContext;
    private File configFile;

    public SystemWriteProcess(Context ccontext) {
        this.internalStorageDir = ccontext.getFilesDir();
        this.currentContext = ccontext;
    }

    public void saveUserConfig(ConfigurationApp confObj) {
        try {
            configFile = new File(internalStorageDir, "userConfig.txt");
            configFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(configFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(confObj);
        } catch (IOException e) {
            Log.d("testeo",e.getMessage().toString());
        }
    }

    public ConfigurationApp readUserConfig() {
        configFile = new File(internalStorageDir, "userConfig.txt");
        ConfigurationApp confObj = new ConfigurationApp();
        try {
            if (configFile.exists()){
                FileInputStream fis = new FileInputStream(configFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                confObj = (ConfigurationApp) ois.readObject();
            }else{
                confObj=new ConfigurationApp();
                confObj.setLockedApps(new ArrayList<String>());
                saveUserConfig(confObj);
            }
            }catch (Exception e) {
                Log.d("testeo",e.getMessage());
            }
        return confObj;
    }

    public ArrayList<App> getAllApps() {
        final PackageManager pm = currentContext.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        ArrayList<App> data = new ArrayList<>();
        for (ApplicationInfo p : packages) {
            App ad = new App();
            ad.setName(p.loadLabel(pm).toString());
            ad.setPackagePath(p.packageName);
            ad.setState(false);
            ad.setIcon(p.loadIcon(pm));
            if ((p.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                continue;
            }
            data.add(ad);
        }
        return data;
    }
    public void lockApp(String pck){
        ConfigurationApp configApp = readUserConfig();
        ArrayList<String> lockedApps = configApp.getLockedApps();
        if(!lockedApps.contains(pck)){
            lockedApps.add(pck);
            configApp.setLockedApps(lockedApps);
            saveUserConfig(configApp);
        }
    }
    public void unlockApp(String pck){
        ConfigurationApp configApp = readUserConfig();
        ArrayList<String> lockedApps = configApp.getLockedApps();
        if(lockedApps.contains(pck)){
            lockedApps.remove(pck);
            configApp.setLockedApps(lockedApps);
            saveUserConfig(configApp);
        }
    }

}