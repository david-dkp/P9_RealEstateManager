package com.openclassrooms.realestatemanager.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclassrooms.realestatemanager.data.models.Estate
import kotlinx.coroutines.flow.Flow

@Dao
interface EstateDao {

    @Query("SELECT * FROM estate_table ORDER BY creation_date_ts")
    fun getEstatesFlow(): Flow<List<Estate>>

    @Query("SELECT * FROM estate_table WHERE user_id =:userId AND need_push = '1' ")
    suspend fun getUnsyncEstatesById(userId: String): List<Estate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEstates(estates: List<Estate>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEstate(estate: Estate)

    @Query("DELETE FROM estate_table")
    suspend fun deleteAllEstates()

    @Query("DELETE FROM estate_table WHERE user_id = :userId")
    suspend fun deleteAllEstatesByUserId(userId: String)
}