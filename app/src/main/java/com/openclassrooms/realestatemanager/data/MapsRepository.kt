package com.openclassrooms.realestatemanager.data

import com.openclassrooms.realestatemanager.data.models.dto.GeocodingResponse
import com.openclassrooms.realestatemanager.others.Resource


interface MapsRepository {

    suspend fun getGeocodingResult(address: String): Resource<GeocodingResponse.Result>

}