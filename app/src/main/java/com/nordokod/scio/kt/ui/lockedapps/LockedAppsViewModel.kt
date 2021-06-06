package com.nordokod.scio.kt.ui.lockedapps

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nordokod.scio.kt.constants.enums.SuccessMessage
import com.nordokod.scio.kt.model.entity.AppPackage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class LockedAppsViewModel(
        application: Application
) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    val appsList = MutableLiveData<List<AppPackage>>()
    val error = MutableLiveData<Exception>()
    val successMessage = MutableLiveData<SuccessMessage>()

    fun getAllApps() {
        viewModelScope.launch {
            try {
                appsList.value = getDeviceAppsProcess()
            } catch ( e : Exception) {
                error.value = e
            }
        }
    }

    private suspend fun getDeviceAppsProcess() = withContext(Dispatchers.Default){
        val packageManager = context.packageManager
        val packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        val data = mutableListOf<AppPackage>()
        for (p in packages) {
            val appPackage = AppPackage(
                    name = p.loadLabel(packageManager).toString(),
                    packagePath = p.packageName,
                    icon = p.loadIcon(packageManager).toBitmap()
            )
            if (p.flags and ApplicationInfo.FLAG_SYSTEM == 1) {
                continue
            }
            data.add(appPackage)
        }
        return@withContext data
    }
}