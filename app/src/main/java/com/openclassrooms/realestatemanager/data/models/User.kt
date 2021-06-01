package com.openclassrooms.realestatemanager.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.openclassrooms.realestatemanager.utils.IdUtils

@Entity(tableName = "user_table")
@IgnoreExtraProperties
data class User(
    @DocumentId
    @PrimaryKey(autoGenerate = false)
    var id: String = IdUtils.generateId(20),

    var email: String? = null,

    @ColumnInfo(name = "first_name")
    @get:PropertyName("first_name") @set:PropertyName("first_name")
    var firstName: String? = null,

    @ColumnInfo(name = "last_name")
    @get:PropertyName("last_name") @set:PropertyName("last_name")
    var lastName: String? = null,

    @ColumnInfo(name = "phone_number")
    @get:PropertyName("phone_number") @set:PropertyName("phone_number")
    var phoneNumber: String? = null,

    @ColumnInfo(name = "image_path")
    @get:PropertyName("image_path") @set:PropertyName("image_path")
    var imagePath: String? = null,

    @ColumnInfo(name = "need_push")
    @Exclude
    var isPushNeeded: Boolean? = false
)
