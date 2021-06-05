package com.nordokod.scio.kt.model.source.local

import androidx.room.*
import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.model.entity.UserWithGuides
@Dao
interface GuideDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGuide(guide: Guide)

    @Update
    suspend fun updateGuide(guide: Guide)

    @Query("Update Guide SET syncState = :syncState WHERE id = :id")
    suspend fun updateSyncState(id: Int,syncState: Int)

    @Delete
    suspend fun deleteGuide(guide: Guide)

    @Transaction
    @Query("SELECT * FROM User where uid = :uid")
    suspend fun getGuidesFromUser(uid: String) : UserWithGuides

    @Query("DELETE FROM Guide")
    suspend fun deleteAllGuides()

}