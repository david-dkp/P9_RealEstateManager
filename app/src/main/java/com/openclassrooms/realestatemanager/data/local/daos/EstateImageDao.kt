package com.openclassrooms.realestatemanager.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclassrooms.realestatemanager.data.models.EstateImage

@Dao
interface EstateImageDao {

    @Query("SELECT * FROM estate_image_table WHERE id = :id")
    suspend fun getEstateImages(id: String): List<EstateImage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEstateImages(estateImages: List<EstateImage>)
}