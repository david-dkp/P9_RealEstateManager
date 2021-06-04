package com.openclassrooms.realestatemanager.data

import com.openclassrooms.realestatemanager.data.models.domain.User
import com.openclassrooms.realestatemanager.others.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun isLoggedInFlow(): Flow<Boolean>
    fun logout()
    suspend fun getUsers(): Resource<List<User>>
    suspend fun addUser(user: User): Resource<Void>
    suspend fun getCurrentUser(): Resource<User>

}