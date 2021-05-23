package com.openclassrooms.realestatemanager.data

import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.models.EstateImage
import com.openclassrooms.realestatemanager.data.models.User
import com.openclassrooms.realestatemanager.others.Resource
import kotlinx.coroutines.flow.Flow

interface EstateRepository {

    fun getEstatesFlow(): Flow<List<Estate>>

    suspend fun getUserEstates(): Resource<List<Estate>>
    suspend fun getEstateImagesByEstateId(estateId: String): Resource<List<EstateImage>>

    suspend fun refreshEstates(): Resource<Void>

    suspend fun addEstate(estate: Estate): Resource<Void>
    suspend fun updateEstate(estate: Estate): Resource<Void>

    suspend fun updateEstateImages(estateId: String, images: List<EstateImage>): Resource<Void>

}