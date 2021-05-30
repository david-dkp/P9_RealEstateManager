package com.openclassrooms.realestatemanager.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclassrooms.realestatemanager.data.models.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user_table")
    suspend fun getUsers(): List<User>

    @Query("SELECT * FROM user_table WHERE id = :id")
    suspend fun getUserById(id: String): User

    @Insert
    suspend fun insertAllUsers(users: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("DELETE FROM user_table")
    suspend fun deleteAllUsers()
}