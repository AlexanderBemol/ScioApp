package com.nordokod.scio;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.nordokod.scio.View.AppsList;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class ScioService extends Service {
    Context currentContext;

    @Override
    public void onCreate() {
        super.onCreate();
        currentContext=getApplicationContext();
        Log.i( "test", "Servicio creado");
    }

    @Override
    public int onStartCommand(Intent intencion, int flags, int idArranque) {
        Log.d("test","onStart");
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                Log.d("test","repite");
                Log.d("test",getForegroundApp());
                if(getForegroundApp().equals("com.whatsapp")){
                    Log.d("test","yayaya");
                    Intent intent=new Intent(currentContext, AppsList.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }
        };
        //Start
        handler.postDelayed(runnable, 1000);
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("test","destruido");
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("test","ConfChange");
    }

    public void onLowMemory() {
        Log.d("test","NoMemoria");
    }

    public void onTrimMemory(int level) {
        Log.d("test","TrimMemoria: "+level);
    }

    public String getForegroundApp() {
        String currentApp = "NULL";
        UsageStatsManager usm = (UsageStatsManager) currentContext.getSystemService(Context.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
        if (appList != null && appList.size() > 0) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
            for (UsageStats usageStats : appList) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (mySortedMap != null && !mySortedMap.isEmpty()) {
                currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
            }
        }
        return currentApp;
    }
}
