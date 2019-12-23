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

import com.google.android.gms.common.api.Batch;
import com.nordokod.scio.constants.KindOfQuestion;
import com.nordokod.scio.entity.ConfigurationApp;
import com.nordokod.scio.entity.Guide;
import com.nordokod.scio.entity.TrueFalseQuestion;
import com.nordokod.scio.view.TrueFalseQuestionDialog;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

public class LockAppProcess extends Service {
    Context currentContext;
    ConfigurationApp confApp;
    static String prevApp;
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
        final Handler handler = new Handler();
        SystemWriteProcess swp = new SystemWriteProcess(getApplicationContext());
        ConfigurationApp configurationApp = swp.readUserConfig();
        prevApp = "";
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                String fgApp=getForegroundApp();

                Log.d("testeo",fgApp);
                if(!fgApp.equals(prevApp)&&!prevApp.equals("android")){
                    if(configurationApp.isAppLocker()){
                        if(configurationApp.getLockedApps().contains(fgApp)){//está bloqueada..
                            Log.d("testeo","oh");
                            QuestionHelper questionHelper = new QuestionHelper();
                            try{
                                questionHelper.showRandomQuestion(getApplicationContext());
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }

                prevApp=fgApp;
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
        try{
            registerReceiver(myBroadCast,new IntentFilter(Intent.ACTION_SCREEN_ON));
            registerReceiver(myBroadCast,new IntentFilter(Intent.ACTION_SCREEN_OFF));
        }catch (Exception e){
            e.printStackTrace();
        }
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
        if (appList != null && appList.size() > 0) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
            for (UsageStats usageStats : appList) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (!mySortedMap.isEmpty()) {
                currentApp = Objects.requireNonNull(mySortedMap.get(mySortedMap.lastKey())).getPackageName();
            }
        }
        return currentApp;
    }


}
