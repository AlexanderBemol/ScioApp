package com.nordokod.scio.Process;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.nordokod.scio.Entidad.ConfigurationApp;

import java.util.List;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

public class LockAppProcess extends Service {
    Context currentContext;
    ConfigurationApp confApp;

    @Override
    public void onCreate() {
        super.onCreate();
        currentContext=getApplicationContext();
        confApp=new ConfigurationApp();
        SystemWriteProcess swp = new SystemWriteProcess(this);
        confApp=swp.readUserConfig();
    }

    @Override
    public int onStartCommand(Intent intencion, int flags, int idArranque) {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                String fgApp=getForegroundApp();
                if(confApp.getLockedApps().contains(fgApp)){//está bloqueada..
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //startActivity(intent);
                }

            }
        };
        //Start
        handler.postDelayed(runnable, 1000);//cada tiempo..
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }


    public void onLowMemory() {

    }

    public void onTrimMemory(int level) {

    }

    public String getForegroundApp() {
        String currentApp = "NULL";
        UsageStatsManager usm = (UsageStatsManager) currentContext.getSystemService(Context.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        List<UsageStats> appList = Objects.requireNonNull(usm).queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
        if (appList != null && appList.size() > 0) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
            for (UsageStats usageStats : appList) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (!mySortedMap.isEmpty()) {
                currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
            }
        }
        return currentApp;
    }


}
