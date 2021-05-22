package com.openclassrooms.realestatemanager.data

import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.others.Resource
import retrofit2.Response


interface MapsRepository {

    suspend fun getPositionFromAddress(address: String): Resource<LatLng>

    suspend fun getMapImageUrlFromAddress(address: String): Resource<String>

}