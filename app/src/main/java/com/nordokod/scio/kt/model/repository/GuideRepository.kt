package com.nordokod.scio.kt.model.repository

import com.nordokod.scio.kt.constants.Generic
import com.nordokod.scio.kt.constants.GuideException
import com.nordokod.scio.kt.constants.PhoneNetworkException
import com.nordokod.scio.kt.constants.enums.SyncState
import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.model.source.local.GuideDAO
import com.nordokod.scio.kt.model.source.remote.IRemoteGuide
import com.nordokod.scio.kt.model.source.remote.RemoteGuide
import com.nordokod.scio.kt.utils.NetworkManager
import com.nordokod.scio.kt.utils.TaskResult
import kotlinx.coroutines.withTimeout

class GuideRepository(
        private val remoteGuide: IRemoteGuide,
        private val localGuide: GuideDAO
) : IGuideRepository {

    override suspend fun createGuide(guide: Guide): TaskResult<Guide> {
        return if (NetworkManager.isOnline()) {
            try {
                withTimeout(Generic.TIMEOUT_VALUE) {
                    when (val result = remoteGuide.createGuide(guide)) {
                        is TaskResult.Success -> {
                            val cloudGuide = result.data
                            cloudGuide.syncState = SyncState.SYNCHRONIZED.code
                            saveGuideOffline(cloudGuide)
                        }
                        is TaskResult.Error -> {
                            guide.syncState = SyncState.ONLY_IN_LOCAL.code
                            saveGuideOffline(guide)
                        }
                    }
                }
            } catch (e: Exception) {
                guide.syncState = SyncState.ONLY_IN_LOCAL.code
                saveGuideOffline(guide)
            }
        } else {
            guide.syncState = SyncState.ONLY_IN_LOCAL.code
            saveGuideOffline(guide)
        }
    }

    override suspend fun updateGuide(guide: Guide): TaskResult<Unit> {
        return if (NetworkManager.isOnline()) {
            try {
                withTimeout(Generic.TIMEOUT_VALUE) {
                    when (remoteGuide.updateGuide(guide)) {
                        is TaskResult.Success -> {
                            guide.syncState = SyncState.SYNCHRONIZED.code
                            updateGuideOffline(guide)
                        }
                        is TaskResult.Error -> {
                            if(guide.syncState != SyncState.ONLY_IN_LOCAL.code)
                                guide.syncState = SyncState.UPDATED_IN_LOCAL.code
                            updateGuideOffline(guide)
                        }
                    }
                }
            } catch (e: Exception) {
                guide.syncState = SyncState.UPDATED_IN_LOCAL.code
                updateGuideOffline(guide)
            }
        } else {
            guide.syncState = SyncState.UPDATED_IN_LOCAL.code
            updateGuideOffline(guide)
        }
    }

    override suspend fun deleteGuide(guide: Guide): TaskResult<Unit> {
        return if (NetworkManager.isOnline()) {
            try {
                withTimeout(Generic.TIMEOUT_VALUE) {
                    when (remoteGuide.deleteGuide(guide.remoteId)) {
                        is TaskResult.Success -> {
                            deleteGuideOffline(guide)
                        }
                        is TaskResult.Error -> {
                            guide.syncState = SyncState.DELETED_IN_LOCAL.code
                            if(guide.remoteId == "")
                                deleteGuideOffline(guide)
                            else
                                updateSyncState(guide)
                        }
                    }
                }
            } catch (e: Exception) {
                guide.syncState = SyncState.DELETED_IN_LOCAL.code
                updateSyncState(guide)
            }
        } else {
            guide.syncState = SyncState.DELETED_IN_LOCAL.code
            updateSyncState(guide)
        }
    }

    override suspend fun getUserGuides(uid: String): TaskResult<List<Guide>> {
        return try {
            val userWithGuides = localGuide.getGuidesFromUser(uid)
            val guidesList = userWithGuides.guides.filter { guide -> guide.syncState != SyncState.DELETED_IN_LOCAL.code }
            if(guidesList.isNotEmpty())
                TaskResult.Success(guidesList)
            else
                TaskResult.Error(GuideException(GuideException.Code.NO_GUIDES))
        } catch (e: Exception) {
            TaskResult.Error(e)
        }
    }

    override suspend fun generateGuideLink(): TaskResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getPublicGuide(): TaskResult<Guide> {
        TODO("Not yet implemented")
    }

    override suspend fun importGuide(): TaskResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun syncInRemote(uid: String): TaskResult<Unit> {
        return try {
            if (NetworkManager.isOnline()) {
                val guides = localGuide.getGuidesFromUser(uid).guides
                guides.forEach {
                    when (it.syncState) {
                        SyncState.ONLY_IN_LOCAL.code -> {
                            withTimeout(Generic.TIMEOUT_VALUE) {
                                val result = remoteGuide.createGuide(it)
                                if (result is TaskResult.Success) {
                                    updateGuideOffline(result.data.apply { syncState = SyncState.SYNCHRONIZED.code })
                                }
                            }
                        }
                        SyncState.UPDATED_IN_LOCAL.code -> {
                            withTimeout(Generic.TIMEOUT_VALUE) {
                                if (it.remoteId.isNotEmpty()) {
                                    if (remoteGuide.updateGuide(it) is TaskResult.Success) {
                                        updateSyncState(it.apply { syncState = SyncState.SYNCHRONIZED.code })
                                    }
                                } else {
                                    val result = remoteGuide.createGuide(it)
                                    if(result is TaskResult.Success){
                                        updateGuide(result.data.apply { syncState = SyncState.SYNCHRONIZED.code })
                                    }
                                }
                            }
                        }
                        SyncState.DELETED_IN_LOCAL.code -> {
                            if (it.remoteId.isNotEmpty()) {
                                withTimeout(Generic.TIMEOUT_VALUE) {
                                    if (remoteGuide.deleteGuide(it.remoteId) is TaskResult.Success)
                                        localGuide.deleteGuide(it)
                                }
                            } else localGuide.deleteGuide(it)
                        }
                    }
                }
                TaskResult.Success(Unit)
            } else TaskResult.Error(PhoneNetworkException())
        } catch (e: Exception) {
            TaskResult.Error(e)
        }
    }

    override suspend fun syncInLocal(): TaskResult<Unit> {
        return try {
            if (NetworkManager.isOnline()) {
                withTimeout(Generic.TIMEOUT_VALUE) {
                    when (val result = remoteGuide.getUserGuides()) {
                        is TaskResult.Success -> {
                            val guides = result.data
                            guides.forEach {
                                localGuide.insertGuide(it.apply { syncState = SyncState.SYNCHRONIZED.code })
                            }
                            TaskResult.Success(Unit)
                        }
                        is TaskResult.Error -> {
                            result
                        }
                    }
                }
            } else TaskResult.Error(PhoneNetworkException())
        } catch (e: Exception) {
            TaskResult.Error(e)
        }
    }

    private fun saveGuideOffline(guide: Guide): TaskResult<Guide> {
        return try {
            localGuide.insertGuide(guide)
            TaskResult.Success(guide)
        } catch (e: Exception) {
            TaskResult.Error(e)
        }
    }

    private fun updateGuideOffline(guide: Guide): TaskResult<Unit> {
        return try {
            localGuide.updateGuide(guide)
            TaskResult.Success(Unit)
        } catch (e: Exception) {
            TaskResult.Error(e)
        }
    }

    private fun updateSyncState(guide: Guide): TaskResult<Unit>{
        return try {
            localGuide.updateSyncState(guide.id,guide.syncState)
            TaskResult.Success(Unit)
        } catch (e: Exception) {
            TaskResult.Error(e)
        }
    }

    private fun deleteGuideOffline(guide: Guide): TaskResult<Unit> {
        return try {
            localGuide.deleteGuide(guide)
            TaskResult.Success(Unit)
        } catch (e: Exception) {
            TaskResult.Error(e)
        }
    }

}