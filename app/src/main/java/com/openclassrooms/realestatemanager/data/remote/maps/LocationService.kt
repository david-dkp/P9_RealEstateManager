package com.openclassrooms.realestatemanager.data.remote.maps

import com.google.android.gms.maps.model.LatLng

interface LocationService {

    suspend fun getCurrentLocation(): LatLng

}