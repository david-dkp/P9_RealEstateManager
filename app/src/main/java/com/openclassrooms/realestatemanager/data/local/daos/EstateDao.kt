package com.openclassrooms.realestatemanager.data.local.daos

import androidx.room.*
import com.openclassrooms.realestatemanager.data.models.Estate
import kotlinx.coroutines.flow.Flow

@Dao
interface EstateDao {

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun insertAllEstate(estates: List<Estate>)

    @Query("SELECT * FROM estate_table")
    fun getEstates(): Flow<List<Estate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEstate(estate: Estate)

    @Update
    suspend fun updateEstate(estate: Estate)
}