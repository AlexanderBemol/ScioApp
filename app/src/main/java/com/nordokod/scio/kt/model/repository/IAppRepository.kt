package com.nordokod.scio.kt.model.repository

import com.nordokod.scio.kt.model.entity.AppPackage
import com.nordokod.scio.kt.utils.TaskResult

interface IAppRepository {
    suspend fun insertApps(apps : List<AppPackage>) : TaskResult<Unit>
    suspend fun deleteApps() : TaskResult<Unit>
    suspend fun getApps() : TaskResult<List<AppPackage>>
}