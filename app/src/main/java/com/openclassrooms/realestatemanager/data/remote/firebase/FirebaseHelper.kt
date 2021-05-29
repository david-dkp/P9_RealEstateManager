package com.openclassrooms.realestatemanager.data.remote.firebase

import android.net.Uri
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.models.EstateImage
import com.openclassrooms.realestatemanager.data.models.User

interface FirebaseHelper {

    suspend fun getUserEstates(): List<Estate>
    suspend fun getEstates(): List<Estate>

    suspend fun getEstateById(id: String): Estate

    suspend fun getEstateImagesByEstateId(estateId: String): List<EstateImage>

    suspend fun uploadEstateImages(estate: Estate, estateImages: List<EstateImage>)

    suspend fun getUsers(): List<User>
    suspend fun addUser(user: User): User

}