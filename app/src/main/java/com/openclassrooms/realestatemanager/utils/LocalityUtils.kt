package com.openclassrooms.realestatemanager.utils

import com.google.android.libraries.places.api.model.AddressComponent
import com.openclassrooms.realestatemanager.data.models.GeocodingResponse
import com.openclassrooms.realestatemanager.others.ADDRESS_COMPONENTS_LOCALITY_TYPES

object LocalityUtils {

    fun getLocalityFromAddressComponents(addressComponents: List<GeocodingResponse.AddressComponent>): String {
        return addressComponents.first {
            it.types.any { type ->
                ADDRESS_COMPONENTS_LOCALITY_TYPES.contains(type)
            }
        }.longName
    }

    fun getLocalityFromMapsAddressComponents(addressComponents: List<AddressComponent>): String {
        return addressComponents.first {
            it.types.any { type ->
                ADDRESS_COMPONENTS_LOCALITY_TYPES.contains(type)
            }
        }.name
    }

}