package com.openclassrooms.realestatemanager.daos

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.firebase.Timestamp
import com.openclassrooms.realestatemanager.data.local.AppDatabase
import com.openclassrooms.realestatemanager.data.local.daos.EstateDao
import com.openclassrooms.realestatemanager.data.models.Estate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class EstateDaoTest {

    lateinit var estateDao: EstateDao

    @Before
    fun setup() {
        val appDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        estateDao = appDatabase.estateDao()
    }

    @Test
    fun insertAndGetEstateTest() {
        runBlocking {
            Estate(
                "someid",
                "An address",
                Timestamp.now(),
                "A description",
                "a preview url",
                500000000.5f,
                5,
                Timestamp.now(),
                55.4f,
                "Appartement",
                "someid",
                true
            ).let {
                estateDao.insertEstate(it)
                val estates = estateDao.getEstatesFlow().first()
                assert(estates.contains(it))
            }
        }
    }

    @Test
    fun updateEstateTest() {
        runBlocking {
            Estate(
                "someid",
                "An address",
                Timestamp.now(),
                "A description",
                "a preview url",
                500000000.5f,
                5,
                Timestamp.now(),
                55.4f,
                "Appartement",
                "someid",
                true
            ).let {
                estateDao.insertEstate(it)
                assert(estateDao.getEstatesFlow().first().contains(it))

                it.address = "A completely different address"

                estateDao.updateEstate(it)

                assert(estateDao.getEstatesFlow().first().contains(it))
            }
        }
    }
}