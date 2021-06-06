package com.nordokod.scio.old.process
import android.app.Activity
import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.nordokod.scio.old.constants.RequestCode

class UpdateCheck {
    fun checkUpdateAvailability (context: Context, activity: Activity){
        val appUpdateManager = AppUpdateManagerFactory.create(context)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if(appUpdateInfo.updateAvailability()== UpdateAvailability.UPDATE_AVAILABLE){
                if(appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) appUpdateManager.startUpdateFlowForResult(appUpdateInfo,AppUpdateType.IMMEDIATE,activity, RequestCode.RC_UPDATE_INMEDIATE.code)
                else if(appUpdateInfo.isUpdateTypeAllowed((AppUpdateType.FLEXIBLE))) appUpdateManager.startUpdateFlowForResult(appUpdateInfo,AppUpdateType.FLEXIBLE,activity, RequestCode.RC_UPDATE_FLEXIBLE.code)
            }
        }
    }



}
