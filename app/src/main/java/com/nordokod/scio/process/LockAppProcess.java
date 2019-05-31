package com.nordokod.scio.process;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.nordokod.scio.entity.ConfigurationApp;

import java.util.ArrayList;
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
        Log.d("testeo","creado");
    }

    @Override
    public int onStartCommand(Intent intentM, int flags, int idArranque) {
        Log.d("testeo","onStart");
        final QuestionLauncherProcess qlp =new QuestionLauncherProcess(currentContext);
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                Log.d("testeo","working");
                String fgApp=getForegroundApp();
                ArrayList<String> appslocked=new ArrayList<>();
                appslocked.add("com.whatsapp");
                appslocked.add("com.twitter.android");
                appslocked.add("com.google.android.youtube");
                if(appslocked.contains(getForegroundApp())){//está bloqueada..
                    qlp.launchQuestionTrueFalse();
                }

            }
        };
        BroadcastReceiver myBroadCast=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (Objects.requireNonNull(intent.getAction())){
                    case Intent.ACTION_SCREEN_ON:
                        Log.d("testeo","screenOn");
                        handler.postDelayed(runnable, 1000);//cada tiempo..

                        break;
                    case Intent.ACTION_SCREEN_OFF:
                        Log.d("testeo","screenOff");
                        handler.removeCallbacks(runnable);

                        break;
                }
            }
        };
        handler.postDelayed(runnable, 1000);//cada tiempo..
        registerReceiver(myBroadCast,new IntentFilter(Intent.ACTION_SCREEN_ON));

        registerReceiver(myBroadCast,new IntentFilter(Intent.ACTION_SCREEN_OFF));
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent=new Intent("com.nordokod.kill");
        sendBroadcast(intent);
        Log.d("testeo","Destroy");
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Intent intent=new Intent("com.nordokod.kill");
        sendBroadcast(intent);
        Log.d("testeo","Removed");
    }


    public IBinder onBind(Intent intent) {
        Log.d("testeo","bind");
        return null;
    }


    public void onLowMemory() {
        Log.d("testeo","noMem");

    }

    public void onTrimMemory(int level) {
        Log.d("testeo","trim");

    }

    public String getForegroundApp() {
        String currentApp = "NULL";
        UsageStatsManager usm = (UsageStatsManager) currentContext.getSystemService(Context.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        List<UsageStats> appList = Objects.requireNonNull(usm).queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
        Log.d("testeo",appList.size()+"");
        if (appList != null && appList.size() > 0) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
            for (UsageStats usageStats : appList) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (!mySortedMap.isEmpty()) {
                currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
            }
        }
        Log.d("testeo",currentApp);
        return currentApp;
    }


}
