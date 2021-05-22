package com.openclassrooms.realestatemanager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.openclassrooms.realestatemanager.data.local.daos.EstateDao
import com.openclassrooms.realestatemanager.data.local.daos.EstateImageDao
import com.openclassrooms.realestatemanager.data.local.daos.UserDao
import com.openclassrooms.realestatemanager.data.local.typeconverters.AppTypeConverter
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.models.EstateImage
import com.openclassrooms.realestatemanager.data.models.User

@TypeConverters(AppTypeConverter::class)
@Database(entities = [User::class, Estate::class, EstateImage::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    lateinit var estateDao: EstateDao
    lateinit var userDao: UserDao
    lateinit var estateImageDao: EstateImageDao

}