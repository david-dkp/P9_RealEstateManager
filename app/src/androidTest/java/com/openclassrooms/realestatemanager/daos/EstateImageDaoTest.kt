package com.openclassrooms.realestatemanager.daos

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.openclassrooms.realestatemanager.data.local.AppDatabase
import com.openclassrooms.realestatemanager.data.local.daos.EstateImageDao
import com.openclassrooms.realestatemanager.data.models.domain.EstateImage
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class EstateImageDaoTest {

    lateinit var estateImageDao: EstateImageDao

    @Before
    fun setup() {
        val appDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        estateImageDao = appDatabase.estateImageDao()
    }

    @Test
    fun insertAndGetEstateImagesByEstateIdTest() {
        val estateImages = listOf(
            EstateImage("1", "Description 1", "1", "Image 1"),
            EstateImage("2", "Description 2", "1", "Image 2"),
            EstateImage("3", "Description 3", "8", "Image 3"),
            EstateImage("4", "Description 4", "4", "Image 4"),
            EstateImage("5", "Description 5", "5", "Image 5"),
            EstateImage("6", "Description 6", "5", "Image 6"),
            EstateImage("7", "Description 7", "4", "Image 7"),
            EstateImage("8", "Description 8", "4", "Image 8")
        )

        val estateId = "4"

        val expectedOutput = listOf(
            EstateImage("4", "Description 4", "4", "Image 4"),
            EstateImage("7", "Description 7", "4", "Image 7"),
            EstateImage("8", "Description 8", "4", "Image 8")
            )

        runBlocking {
            estateImageDao.insertAllEstateImages(estateImages)

            assert(estateImageDao.getEstateImagesByEstateId(estateId).containsAll(expectedOutput))
        }
    }

}