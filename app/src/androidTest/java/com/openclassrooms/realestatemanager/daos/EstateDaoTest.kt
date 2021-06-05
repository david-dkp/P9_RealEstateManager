package com.openclassrooms.realestatemanager.daos

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.openclassrooms.realestatemanager.data.local.AppDatabase
import com.openclassrooms.realestatemanager.data.local.daos.EstateDao
import com.openclassrooms.realestatemanager.data.models.domain.Estate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class EstateDaoTest {

    lateinit var appDatabase: AppDatabase

    lateinit var estateDao: EstateDao

    @Before
    fun setup() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        estateDao = appDatabase.estateDao()
    }

    @Test
    fun insertWithSuccess() {
        runBlocking {
            val estates = listOf(
                Estate(address = "Random address"),
                Estate(description = "Random desc"),
                Estate(roomCount = 45),
                Estate(bedroomCount = 12),
            )

            estateDao.insertAllEstates(estates)

            val storedEstates = estateDao.getEstatesFlow().first()

            assert(storedEstates == estates)
        }

    }

    @Test
    fun deleteWithSuccess() {
        runBlocking {
            val estates = listOf(
                Estate(address = "Random address"),
                Estate(description = "Random desc"),
                Estate(roomCount = 45),
                Estate(bedroomCount = 12),
            )

            estateDao.insertAllEstates(estates)

            var storedEstates = estateDao.getEstatesFlow().first()

            assert(storedEstates == estates)

            estateDao.deleteAllEstates()

            storedEstates = estateDao.getEstatesFlow().first()

            assert(storedEstates.isEmpty())

        }

    }

    @After
    fun after() {
        appDatabase.close()
    }

}