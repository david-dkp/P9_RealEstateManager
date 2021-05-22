package com.openclassrooms.realestatemanager.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName

@Entity(tableName = "estate_table")
data class Estate(
    @DocumentId
    @PrimaryKey(autoGenerate = false)
    var id: String? = null,

    var address: String,

    @ColumnInfo(name = "creation_date_ts")
    @SerializedName("creation_date_ts")
    var creationDateTs: Timestamp,

    var description: String,

    @ColumnInfo(name = "preview_image_uri")
    @SerializedName("preview_image_uri")
    var previewImageUri: String,

    var price: Float,

    @ColumnInfo(name = "room_count")
    @SerializedName("room_count")
    var roomCount: Int,

    @ColumnInfo(name = "sale_date_ts")
    @SerializedName("sale_date_ts")
    var saleDateTs: Timestamp,

    @ColumnInfo(name = "surface_area")
    @SerializedName("surface_area")
    var surfaceArea: Float,

    var type: String,

    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    var userId: String,

    @ColumnInfo(name = "need_push")
    @Exclude
    var isPushNeeded: Boolean = false
)
