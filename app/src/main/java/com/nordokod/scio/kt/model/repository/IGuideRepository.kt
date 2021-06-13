package com.nordokod.scio.kt.model.repository

import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.utils.TaskResult

interface IGuideRepository {
    suspend fun createGuide(guide: Guide): TaskResult<Guide>
    suspend fun updateGuide(guide: Guide): TaskResult<Unit>
    suspend fun deleteGuide(guide: Guide): TaskResult<Unit>
    suspend fun getUserGuides(uid: String): TaskResult<List<Guide>>
    suspend fun getGuide(id : Int): TaskResult<Guide>
    suspend fun generateGuideLink(): TaskResult<String>
    suspend fun getPublicGuide(): TaskResult<Guide>
    suspend fun importGuide(): TaskResult<Unit>
    suspend fun syncInRemote(uid: String): TaskResult<Unit>
    suspend fun syncInLocal(): TaskResult<Unit>

}