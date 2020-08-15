package com.nordokod.scio.kt.services

import android.app.Service
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.nordokod.scio.kt.constants.Generic
import java.util.*

class LockAppService : Service() {
    lateinit var handler: Handler


    override fun onCreate() {
        super.onCreate()
        handler = Handler(Looper.getMainLooper())
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handler.post(object : Runnable {
            override fun run() {
                checkApps()
                handler.postDelayed(this, Generic.CHECK_TIME)
            }
        })

        return super.onStartCommand(intent, flags, startId)
    }

    private fun checkApps() {
        TODO()
    }

    private fun getForegroundApp() : String{
        val usageStatsManager = applicationContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val currentTime = System.currentTimeMillis()
        val appList = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                currentTime - Generic.CHECK_TIME * Generic.CHECK_TIME,
                currentTime
        )
        if(appList!=null && appList.size>0){
            val sortedMap = sortedMapOf<Long,UsageStats>()
            appList.forEach{
                sortedMap[it.lastTimeUsed] = it
            }
            if(!sortedMap.isEmpty()) return sortedMap[sortedMap.lastKey()]?.packageName.toString()
        }
        return ""
    }

}