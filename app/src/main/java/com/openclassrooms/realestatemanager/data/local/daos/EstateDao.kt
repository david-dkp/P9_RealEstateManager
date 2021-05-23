package com.openclassrooms.realestatemanager.data.local.daos

import androidx.room.*
import com.openclassrooms.realestatemanager.data.models.Estate
import kotlinx.coroutines.flow.Flow

@Dao
interface EstateDao {

    @Query("SELECT * FROM estate_table")
    fun getEstatesFlow(): Flow<List<Estate>>

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun insertAllEstates(estates: List<Estate>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEstate(estate: Estate)

    @Update
    suspend fun updateEstate(estate: Estate)

    @Query("DELETE FROM estate_table")
    suspend fun deleteAllEstates()

    @Query("DELETE FROM estate_table WHERE user_id = :userId")
    suspend fun deleteAllEstatesByUserId(userId: String)
}