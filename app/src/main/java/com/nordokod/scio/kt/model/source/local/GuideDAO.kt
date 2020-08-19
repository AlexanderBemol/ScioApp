package com.nordokod.scio.kt.model.source.local

import androidx.room.*
import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.model.entity.UserWithGuides
@Dao
interface GuideDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGuide(guide: Guide)

    @Update
    fun updateGuide(guide: Guide)

    @Query("Update Guide SET syncState = :syncState WHERE id = :id")
    fun updateSyncState(id: Int,syncState: Int)

    @Delete
    fun deleteGuide(guide: Guide)

    @Transaction
    @Query("SELECT * FROM User where uid = :uid")
    fun getGuidesFromUser(uid: String) : UserWithGuides

    @Query("DELETE FROM Guide")
    fun deleteAllGuides()

}