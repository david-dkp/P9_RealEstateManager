package com.openclassrooms.realestatemanager.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.openclassrooms.realestatemanager.contracts.AppDatabaseContract.User
import com.openclassrooms.realestatemanager.utils.IdUtils

@Entity(tableName = User.TABLE_NAME)
@IgnoreExtraProperties
data class User(
    @ColumnInfo(name = User.ID)
    @DocumentId
    @PrimaryKey(autoGenerate = false)
    var id: String = IdUtils.generateId(20),

    @ColumnInfo(name = User.EMAIL)
    @get:PropertyName(User.EMAIL) @set:PropertyName(User.EMAIL)
    var email: String? = null,

    @ColumnInfo(name = User.FIRST_NAME)
    @get:PropertyName(User.FIRST_NAME) @set:PropertyName(User.FIRST_NAME)
    var firstName: String? = null,

    @ColumnInfo(name = User.LAST_NAME)
    @get:PropertyName(User.LAST_NAME) @set:PropertyName(User.LAST_NAME)
    var lastName: String? = null,

    @ColumnInfo(name = User.PHONE_NUMBER)
    @get:PropertyName(User.PHONE_NUMBER) @set:PropertyName(User.PHONE_NUMBER)
    var phoneNumber: String? = null,

    @ColumnInfo(name = User.IMAGE_PATH)
    @get:PropertyName(User.IMAGE_PATH) @set:PropertyName(User.IMAGE_PATH)
    var imagePath: String? = null,

    @ColumnInfo(name = User.NEED_PUSH)
    @get:Exclude @set:Exclude
    var isPushNeeded: Boolean? = false
)
