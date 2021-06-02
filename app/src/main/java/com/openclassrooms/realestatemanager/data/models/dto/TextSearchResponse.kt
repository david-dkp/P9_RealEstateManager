package com.openclassrooms.realestatemanager.data.models.dto

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.Expose

data class TextSearchResponse(

    @Expose
    val results: List<Result>,

    @Expose
    val status: String
) {

    data class Result(
        @Expose
        val geometry: Geometry,

        @Expose
        val types: List<String>
    )

    data class Geometry(
        @Expose
        val location: Location
    )

    data class Location(
        @Expose
        val lat: Float,

        @Expose
        val lng: Float
    ) {
        fun toLatLng() = LatLng(lat.toString().toDouble(), lng.toString().toDouble())
    }

}