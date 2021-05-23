package com.openclassrooms.realestatemanager.data

import com.openclassrooms.realestatemanager.data.models.User
import com.openclassrooms.realestatemanager.others.Resource

interface UserRepository {

    suspend fun getUsers(): Resource<List<User>>
    suspend fun addUser(user: User): Resource<Void>
}