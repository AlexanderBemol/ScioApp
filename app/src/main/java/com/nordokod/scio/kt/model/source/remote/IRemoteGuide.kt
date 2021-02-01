package com.nordokod.scio.kt.model.source.remote

import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.utils.TaskResult

interface IRemoteGuide {
    suspend fun createGuide(guide: Guide): TaskResult<Guide>
    suspend fun updateGuide(guide: Guide): TaskResult<Unit>
    suspend fun deleteGuide(id: String): TaskResult<Unit>
    suspend fun getUserGuides(): TaskResult<List<Guide>>
    suspend fun generateGuideLink(guide: Guide): TaskResult<String>
    suspend fun getPublicGuide(pendingDynamicLinkData: PendingDynamicLinkData): TaskResult<Guide>
    suspend fun importGuide(guide: Guide): TaskResult<Unit>
}