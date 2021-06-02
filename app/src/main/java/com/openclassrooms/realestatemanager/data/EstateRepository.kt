package com.openclassrooms.realestatemanager.data

import com.openclassrooms.realestatemanager.data.models.domain.Estate
import com.openclassrooms.realestatemanager.data.models.domain.EstateImage
import com.openclassrooms.realestatemanager.others.Resource
import kotlinx.coroutines.flow.Flow

interface EstateRepository {

    fun getEstatesFlow(): Flow<List<Estate>>

    suspend fun getUserEstates(): Resource<List<Estate>>

    suspend fun getEstateById(id: String): Resource<Estate>

    suspend fun getEstateImagesByEstateId(estateId: String): Resource<List<EstateImage>>

    suspend fun refreshEstates(): Resource<Void>

    suspend fun uploadEstateImages(estate: Estate, images: List<EstateImage>): Resource<Void>

}