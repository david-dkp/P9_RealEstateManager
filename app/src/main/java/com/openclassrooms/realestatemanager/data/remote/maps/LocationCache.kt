package com.openclassrooms.realestatemanager.data.remote.maps

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.others.PREF_KEY_LAST_LOCATION_LATITUDE
import com.openclassrooms.realestatemanager.others.PREF_KEY_LAST_LOCATION_LONGITUDE

class LocationCache(context: Context) {
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getLastKnownLocation(): LatLng? {
        val latitude = sharedPreferences.getFloat(PREF_KEY_LAST_LOCATION_LATITUDE, 0f)
        val longitude = sharedPreferences.getFloat(PREF_KEY_LAST_LOCATION_LONGITUDE, 0f)

        if (latitude == 0f && longitude == 0f) return null

        return LatLng(latitude.toDouble(), longitude.toDouble())
    }

    fun storeLocation(location: LatLng) {
        sharedPreferences.edit {
            putFloat(PREF_KEY_LAST_LOCATION_LATITUDE, location.latitude.toFloat())
            putFloat(PREF_KEY_LAST_LOCATION_LONGITUDE, location.longitude.toFloat())
            commit()
        }
    }

}