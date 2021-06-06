package com.nordokod.scio.kt.model.source.local

import androidx.room.*
import com.nordokod.scio.kt.model.entity.AppPackage

@Dao
interface AppDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApps(vararg app: AppPackage)

    @Query("DELETE FROM AppPackage")
    suspend fun deleteApps()

    @Query("SELECT * FROM AppPackage")
    suspend fun getApps() : List<AppPackage>
}