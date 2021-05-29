package com.openclassrooms.realestatemanager.utils

import android.content.Context
import android.location.LocationManager
import androidx.core.location.LocationManagerCompat

object LocationUtils {

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

}