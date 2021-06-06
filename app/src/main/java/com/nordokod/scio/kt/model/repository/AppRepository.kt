package com.nordokod.scio.kt.model.repository

import com.nordokod.scio.kt.model.entity.AppPackage
import com.nordokod.scio.kt.model.source.local.AppDAO
import com.nordokod.scio.kt.utils.TaskResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(
        private val appDAO: AppDAO
) : IAppRepository {
    override suspend fun insertApps(apps: List<AppPackage>): TaskResult<Unit> {
        return withContext(Dispatchers.IO){
            try {
                appDAO.insertApps(*apps.toTypedArray())
                TaskResult.Success(Unit)
            }catch (e : Exception){
                TaskResult.Error(e)
            }
        }
    }

    override suspend fun deleteApps(): TaskResult<Unit> {
        return withContext(Dispatchers.IO){
            try {
                appDAO.deleteApps()
                TaskResult.Success(Unit)
            }catch (e : Exception){
                TaskResult.Error(e)
            }
        }
    }

    override suspend fun getApps(): TaskResult<List<AppPackage>> {
        return withContext(Dispatchers.IO){
            try {
                TaskResult.Success(appDAO.getApps())
            }catch (e : Exception){
                TaskResult.Error(e)
            }
        }
    }
}