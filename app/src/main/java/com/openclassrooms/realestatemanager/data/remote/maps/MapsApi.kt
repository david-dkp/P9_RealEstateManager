package com.openclassrooms.realestatemanager.data.remote.maps

import com.openclassrooms.realestatemanager.data.models.GeocodingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryName

interface MapsApi {

    @GET("/geocode/json?component=country:US")
    suspend fun getGeocoding(@Query("address") address: String, @Query("language") language: String): Response<GeocodingResponse>

    @GET("/staticmap/json")
    suspend fun getStaticMapImage(@Query("center") address: String, @Query("zoom") zoom: Int): Response<String>

}