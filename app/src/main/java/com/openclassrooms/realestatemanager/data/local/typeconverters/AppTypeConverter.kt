package com.openclassrooms.realestatemanager.data.local.typeconverters

import androidx.room.TypeConverter
import com.google.firebase.Timestamp
import java.util.*

class AppTypeConverter {

    @TypeConverter
    fun fromTimestamp(timestamp: Timestamp): Long = timestamp.toDate().time

    @TypeConverter
    fun toTimestamp(timestampLong: Long): Timestamp = Timestamp(Date(timestampLong))
}