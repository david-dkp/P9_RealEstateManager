package com.openclassrooms.realestatemanager.data.local.daos

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclassrooms.realestatemanager.data.models.domain.EstateImage

@Dao
interface EstateImageDao {

    @Query("SELECT * FROM estate_image_table WHERE estate_id = :estateId")
    suspend fun getEstateImagesByEstateId(estateId: String): List<EstateImage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEstateImages(estateImages: List<EstateImage>)

    @Query("DELETE FROM estate_image_table WHERE estate_id = :estateId")
    suspend fun deleteAllImagesByEstateId(estateId: String)

    @Query("SELECT * FROM estate_image_table")
    fun getEstateImagesCursor(): Cursor

    @Query("SELECT * FROM estate_image_table WHERE id = :id")
    fun getEstateImageByIdCursor(id: String): Cursor
}