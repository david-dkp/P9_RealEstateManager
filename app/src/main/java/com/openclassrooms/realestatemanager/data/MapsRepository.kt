package com.openclassrooms.realestatemanager.data

import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.data.models.GeocodingResponse
import com.openclassrooms.realestatemanager.others.Resource


interface MapsRepository {

    suspend fun getGeocodingResult(address: String): Resource<GeocodingResponse.Result>
    suspend fun getLocation(): Resource<LatLng>

}