package com.openclassrooms.realestatemanager.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GeocodingResponse(
    @Expose
    val results: List<Result>,

    @Expose
    val status: String
){

    data class Result(
        @Expose
        @SerializedName("formatted_address")
        val formattedAddress: String,

        @Expose
        val geometry: Geometry
    )

    data class Geometry(
        @Expose
        val location: Location
    )

    data class Location(
        @Expose
        val lat: Double,

        @Expose
        val lng: Double
    )

}