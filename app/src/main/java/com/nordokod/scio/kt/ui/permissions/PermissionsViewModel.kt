package com.nordokod.scio.kt.ui.permissions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.nordokod.scio.kt.constants.enums.Permissions
import com.nordokod.scio.kt.utils.PermissionsCheck

class PermissionsViewModel(
        private val permissionsCheck: PermissionsCheck,
        application: Application
        ) : AndroidViewModel(application) {
    val haveAutoStart = MutableLiveData<Boolean>()
    val isSystemAlertWindow = MutableLiveData<Boolean>()
    val isUsageStats = MutableLiveData<Boolean>()
    //val error = MutableLiveData<Error>()
    //val permissionsActions = MutableLiveData<PermissionsActions>()

    private val autoStart = permissionsCheck.haveAutoStart()
    private val context = getApplication<Application>().applicationContext

    fun haveAutoStart(){
        haveAutoStart.value = autoStart
    }

    fun checkPermissions(){
        isSystemAlertWindow.value = permissionsCheck.permissionIsGranted(Permissions.SYSTEM_ALERT_WINDOW,context)
        isUsageStats.value = permissionsCheck.permissionIsGranted(Permissions.USAGE_STATS,context)
    }

}