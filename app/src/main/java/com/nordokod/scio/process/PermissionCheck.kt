package com.nordokod.scio.process

import android.Manifest
import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.content.ComponentName
import java.lang.Exception


class PermissionCheck {
    fun permissionIsGranted(permission : PermissionEnum, activity : Activity, context: Context):Boolean{
        return when(permission){
            PermissionEnum.SYSTEM_ALERT_WINDOW ->
                {
                    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Settings.canDrawOverlays(activity)
                    } else {
                        true
                    }
                }
            PermissionEnum.AUTO_START -> false
            PermissionEnum.USAGE_STATS -> {
                val appOps : AppOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
                val mode : Int
                mode = if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
                    appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,android.os.Process.myUid(), context.packageName)
                }else{
                    appOps.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,android.os.Process.myUid(), context.packageName)
                }
                return if (mode == AppOpsManager.MODE_DEFAULT) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        (context.checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED)
                    } else {
                        (context.checkCallingOrSelfPermission("usagestats") == PackageManager.PERMISSION_GRANTED)
                    }
                } else {
                    (mode == AppOpsManager.MODE_ALLOWED)
                }
            }
        }
    }


    fun askForPermission(permission : PermissionEnum, activity : Activity){
        when(permission){
            PermissionEnum.AUTO_START -> autostartPermission(activity)
            PermissionEnum.SYSTEM_ALERT_WINDOW -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                    activity.startActivity(intent)
                }
            }
            PermissionEnum.USAGE_STATS -> {
                val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                activity.startActivity(intent)
            }
        }
    }

    fun autostartPermission(activity: Activity){
        try {
            val intent = Intent()
            val manufacturer = Build.MANUFACTURER
            if ("xiaomi".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")
            } else if ("oppo".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")
            } else if ("vivo".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")
            } else if ("Letv".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")
            } else if ("Honor".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")
            }

            val list = activity.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            if (list.size > 0) {
                activity.startActivity(intent)
            }
        }catch (e : Exception){
            e.stackTrace
        }

    }

    fun haveAutostart() : Boolean{
        val manufacturer = Build.MANUFACTURER
        return when {
            "xiaomi".equals(manufacturer, ignoreCase = true) -> true
            "oppo".equals(manufacturer, ignoreCase = true) -> true
            "vivo".equals(manufacturer, ignoreCase = true) -> true
            "Letv".equals(manufacturer, ignoreCase = true) -> true
            "Honor".equals(manufacturer, ignoreCase = true) -> true
            else -> false
        }
    }
    enum class PermissionEnum{
        AUTO_START,
        SYSTEM_ALERT_WINDOW,
        USAGE_STATS
    }
}