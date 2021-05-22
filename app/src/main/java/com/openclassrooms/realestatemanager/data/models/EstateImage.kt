package com.openclassrooms.realestatemanager.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.gson.annotations.SerializedName

@Entity(tableName = "estate_image_table")
data class EstateImage(
    @DocumentId
    @PrimaryKey(autoGenerate = false)
    var id: String? = null,

    var description: String,

    @ColumnInfo(name = "estate_id")
    @SerializedName("estate_id")
    var estateId: String,

    @ColumnInfo(name = "image_uri")
    @SerializedName("image_uri")
    var imageUri: String
)
