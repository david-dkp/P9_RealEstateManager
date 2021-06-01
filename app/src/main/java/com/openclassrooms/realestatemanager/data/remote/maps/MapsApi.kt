package com.openclassrooms.realestatemanager.data.remote.maps

import com.openclassrooms.realestatemanager.data.models.GeocodingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MapsApi {

    @GET("geocode/json?component=country:US")
    suspend fun getGeocoding(
        @Query("address") address: String,
        @Query("language") language: String,
        @Query("key") apiKey: String
    ): Response<GeocodingResponse>

    //@GET("place/textsearch/json")

}