package com.openclassrooms.realestatemanager.data.models.dto

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GeocodingResponse(
    @Expose
    val results: List<Result>,

    @Expose
    val status: String,

    @SerializedName("error_message")
    @Expose
    val errorMessage: String?
) {

    data class Result(
        @Expose
        @SerializedName("formatted_address")
        val formattedAddress: String,

        @Expose
        @SerializedName("address_components")
        val addressComponents: List<AddressComponent>,

        @Expose
        val geometry: Geometry
    )

    data class AddressComponent(
        @Expose
        @SerializedName("long_name")
        val longName: String,

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