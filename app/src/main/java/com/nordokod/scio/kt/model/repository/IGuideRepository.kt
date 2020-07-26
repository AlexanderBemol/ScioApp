package com.nordokod.scio.kt.model.repository

import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.utils.TaskResult

interface IGuideRepository {
    suspend fun createGuide(guide: Guide): TaskResult<Guide>
    suspend fun updateGuide(guide: Guide): TaskResult<Unit>
    suspend fun deleteGuide(guide: Guide): TaskResult<Unit>
    suspend fun getUserGuides(): TaskResult<ArrayList<Guide>>
    suspend fun generateGuideLink(): TaskResult<String>
    suspend fun getPublicGuide(): TaskResult<Guide>
    suspend fun importGuide(): TaskResult<Unit>

}