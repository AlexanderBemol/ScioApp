package com.nordokod.scio.Process;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.nordokod.scio.Entidad.ConfigurationApp;
import com.nordokod.scio.Entidad.appData;

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

    public SystemWriteProcess(Context ccontext){
        this.internalStorageDir=currentContext.getFilesDir();
        this.currentContext=ccontext;
    }

    public void saveUserConfig(ConfigurationApp confObj){
        try {
            configFile = new File(internalStorageDir,"userConfig.txt");
            FileOutputStream fos = new FileOutputStream(configFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(confObj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ConfigurationApp readUserConfig(){
        configFile = new File(internalStorageDir,"userConfig.txt");
        ConfigurationApp confObj = null;
        if(configFile.exists()){
            
            try {
                FileInputStream fis= new FileInputStream(configFile);
                ObjectInputStream ois= new ObjectInputStream(fis);
                confObj= (ConfigurationApp) ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return confObj;
    }
    public ArrayList<appData> getAllAppsFT(){//porPrimeraVez
        final PackageManager pm = currentContext.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        ArrayList<appData> data=new ArrayList<>();
        for (ApplicationInfo p : packages) {
            appData ad=new appData();
            ad.setAppName(p.loadLabel(pm).toString());
            ad.setAppPackage(p.packageName);
            ad.setAppState(false);
            ad.setIcon(p.loadIcon(pm));
            if((p.flags&ApplicationInfo.FLAG_SYSTEM)==1){
                continue;
            }
            data.add(ad);
        }

        return data;
    }


}