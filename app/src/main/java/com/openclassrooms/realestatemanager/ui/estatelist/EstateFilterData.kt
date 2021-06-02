package com.openclassrooms.realestatemanager.ui.estatelist

import com.google.android.libraries.places.api.model.Place

data class EstateFilterData(
    val priceRange: Pair<Long, Long>,
    val nearTypes: List<Place.Type>,
    val surfaceRange: Pair<Int, Int>,
    val photoCount: Int? = null,
    val isForSale: Boolean? = null
)