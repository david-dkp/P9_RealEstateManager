package com.openclassrooms.realestatemanager.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.gson.annotations.SerializedName

@Entity(tableName = "estate_image_table")
data class EstateImage(
    @DocumentId
    @PrimaryKey(autoGenerate = false)
    var id: String,

    var description: String,

    @ColumnInfo(name = "estate_id")
    @SerializedName("estate_id")
    var estateId: String,

    @ColumnInfo(name = "image_path")
    @SerializedName("image_path")
    var imagePath: String,

    @ColumnInfo(name = "need_push")
    @Exclude
    var isPushNeeded: Boolean = false,

    @Exclude
    var uri: String? = null
)
