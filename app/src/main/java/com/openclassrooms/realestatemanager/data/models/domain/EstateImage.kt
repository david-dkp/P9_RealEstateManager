package com.openclassrooms.realestatemanager.data.models.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName
import com.openclassrooms.realestatemanager.contracts.AppDatabaseContract.EstateImage
import com.openclassrooms.realestatemanager.utils.IdUtils

@Entity(tableName = EstateImage.TABLE_NAME)
@IgnoreExtraProperties
data class EstateImage(

    @ColumnInfo(name = EstateImage.ID)
    @DocumentId
    @PrimaryKey(autoGenerate = false)
    var id: String = IdUtils.generateId(20),

    @ColumnInfo(name = EstateImage.DESCRIPTION)
    @get:SerializedName(EstateImage.DESCRIPTION) @set:SerializedName(EstateImage.DESCRIPTION)
    var description: String? = null,

    @ColumnInfo(name = EstateImage.ESTATE_ID)
    @get:SerializedName(EstateImage.ESTATE_ID) @set:SerializedName(EstateImage.ESTATE_ID)
    var estateId: String? = null,

    @ColumnInfo(name = EstateImage.IMAGE_PATH)
    @get:SerializedName(EstateImage.IMAGE_PATH) @set:SerializedName(EstateImage.IMAGE_PATH)
    var imagePath: String? = null,

    @ColumnInfo(name = EstateImage.URI)
    @get:Exclude @set:Exclude
    var uri: String? = null
)