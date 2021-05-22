package com.openclassrooms.realestatemanager.data.remote.user

import com.google.android.gms.tasks.Task
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.models.EstateImage
import com.openclassrooms.realestatemanager.data.models.User

interface FirebaseHelper {

    suspend fun getUserEstates(): List<Estate>
    suspend fun getEstates(): List<Estate>
    suspend fun getEstateImagesByEstateId(estateId: String): List<EstateImage>

    suspend fun addEstate(estate: Estate)
    suspend fun updateEstate(estate: Estate)

    suspend fun updateEstateImages(estateId: String, images: List<EstateImage>)

    suspend fun getUsers(): List<User>
    suspend fun addUser(user: User)
}