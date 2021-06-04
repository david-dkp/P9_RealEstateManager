package com.openclassrooms.realestatemanager.ui.estatelist

import com.google.android.libraries.places.api.model.Place
import java.io.Serializable

data class EstateFilterData(
    val priceRange: LongRange,
    val nearTypes: List<Place.Type>,
    val surfaceRange: IntRange,
    val type: String? = null,
    val photoCount: Int? = null,
    val isForSale: Boolean? = null
) : Serializable