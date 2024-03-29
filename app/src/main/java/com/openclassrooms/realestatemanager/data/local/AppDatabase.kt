package com.openclassrooms.realestatemanager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.openclassrooms.realestatemanager.data.local.daos.EstateDao
import com.openclassrooms.realestatemanager.data.local.daos.EstateImageDao
import com.openclassrooms.realestatemanager.data.local.daos.UserDao
import com.openclassrooms.realestatemanager.data.local.typeconverters.AppTypeConverter
import com.openclassrooms.realestatemanager.data.models.domain.Estate
import com.openclassrooms.realestatemanager.data.models.domain.EstateImage
import com.openclassrooms.realestatemanager.data.models.domain.User
import com.openclassrooms.realestatemanager.others.APP_DATABASE_VERSION

@TypeConverters(AppTypeConverter::class)
@Database(
    entities = [User::class, Estate::class, EstateImage::class],
    version = APP_DATABASE_VERSION
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun estateDao(): EstateDao
    abstract fun userDao(): UserDao
    abstract fun estateImageDao(): EstateImageDao

}