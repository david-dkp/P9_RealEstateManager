package com.openclassrooms.realestatemanager.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.openclassrooms.realestatemanager.utils.IdUtils

@Entity(tableName = "estate_image_table")
@IgnoreExtraProperties
data class EstateImage(
    @DocumentId
    @PrimaryKey(autoGenerate = false)
    var id: String = IdUtils.generateId(20),

    var description: String? = null,

    @ColumnInfo(name = "estate_id")
    @get:PropertyName("estate_id") @set:PropertyName("estate_id")
    var estateId: String? = null,

    @ColumnInfo(name = "image_path")
    @get:PropertyName("image_path") @set:PropertyName("image_path")
    var imagePath: String? = null,

    @get:Exclude @set:Exclude
    var uri: String? = null
)
