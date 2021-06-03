package com.openclassrooms.realestatemanager.utils

import com.openclassrooms.realestatemanager.data.models.responses.GeocodingResponse
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LocalityUtilsTest {

    @Test
    fun `get locality from a list of components`() {
        val components = listOf(
            GeocodingResponse.AddressComponent(
                "48",
                listOf("street_number")
            ),
            GeocodingResponse.AddressComponent(
                "02154",
                listOf("postal_code")
            ),
            GeocodingResponse.AddressComponent(
                "California",
                listOf("administrative_area_level_1", "political")
            ),
            GeocodingResponse.AddressComponent(
                "Pyrmont",
                listOf("locality", "political")
            ),
        )

        val expectedLocalityName = "California"

        assert(LocalityUtils.getLocalityFromAddressComponents(components) == expectedLocalityName)
    }

}