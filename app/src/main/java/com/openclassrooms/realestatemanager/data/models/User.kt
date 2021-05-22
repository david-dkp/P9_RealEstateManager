package com.openclassrooms.realestatemanager.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_table")
data class User(
    @DocumentId
    @PrimaryKey(autoGenerate = false)
    var id: String? = null,

    var email: String,

    @ColumnInfo(name = "first_name")
    @SerializedName("first_name")
    var firstName: String,

    @ColumnInfo(name = "last_name")
    @SerializedName("last_name")
    var lastName: String,

    @ColumnInfo(name = "phone_number")
    @SerializedName("phone_number")
    var phoneNumber: String,

    @ColumnInfo(name = "photo_uri")
    @SerializedName("phone_uri")
    var phoneUri: String
)
