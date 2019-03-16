package com.nordokod.scio.Process;

import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SystemWriteProcess {
    private File internalStorageDir;
    private Context currentContext;
    private File configFile;

    public  SystemWriteProcess(Context ccontext){
        this.internalStorageDir=currentContext.getFilesDir();
        this.currentContext=ccontext;

    }
    public void saveUserConfig(){
        configFile = new File(internalStorageDir,"userConfig.txt");
        try {
            FileWriter fw = new FileWriter(configFile);
            fw.write
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}