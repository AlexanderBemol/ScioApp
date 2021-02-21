package com.nordokod.scio.kt.utils

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import com.nordokod.scio.kt.constants.enums.Permissions

class PermissionsCheck {
    fun permissionIsGranted(permission : Permissions, context: Context):Boolean{
        return when(permission){
            Permissions.SYSTEM_ALERT_WINDOW -> {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Settings.canDrawOverlays(context)
                } else {
                    true
                }
            }
            Permissions.AUTO_START -> false
            Permissions.USAGE_STATS -> {
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
    fun haveAutoStart() : Boolean {
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
}