package com.openclassrooms.realestatemanager.daos

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.openclassrooms.realestatemanager.data.local.AppDatabase
import com.openclassrooms.realestatemanager.data.local.daos.UserDao
import com.openclassrooms.realestatemanager.data.models.domain.User
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class UserDaoTest {

    lateinit var userDao: UserDao

    @Before
    fun setup() {
        val appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        userDao = appDatabase.userDao()
    }

    @Test
    fun insertAndGetUserTest() {
        runBlocking {
            User("anId", "something@]gmail.com", "Alba", "Varo", "0746586245", "anuri").let {
                userDao.insertAllUsers(listOf(it))
                assert(userDao.getUsers().contains(it))
            }
        }
    }

}