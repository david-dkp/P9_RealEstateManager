package com.openclassrooms.realestatemanager.data

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.preference.PreferenceManager
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.data.models.GeocodingResponse
import com.openclassrooms.realestatemanager.data.remote.maps.LocationService
import com.openclassrooms.realestatemanager.data.remote.maps.MapsApi
import com.openclassrooms.realestatemanager.others.ErrorType
import com.openclassrooms.realestatemanager.others.KEY_LOCATION_LATITUDE
import com.openclassrooms.realestatemanager.others.Resource
import com.openclassrooms.realestatemanager.utils.LocationUtils
import com.openclassrooms.realestatemanager.utils.PermissionUtils
import com.openclassrooms.realestatemanager.utils.Utils
import java.util.*

class AppMapsRepository(
    val context: Context,
    val mapsApi: MapsApi,
    val locationService: LocationService
) : MapsRepository {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    override suspend fun getGeocoding(address: String): Resource<GeocodingResponse> {

        if (!Utils.isInternetAvailable(context)) {
            return Resource.Error(errorType = ErrorType.NoInternet)
        }

        return try {
            val response = mapsApi.getGeocoding(
                address,
                Locale.getDefault().language,
                BuildConfig.MAPS_API_KEY
            )

            if (response.isSuccessful) {
                if (response.body()!!.results.isEmpty()) {
                    Resource.Error(errorType = ErrorType.CantFoundAddress)
                } else {
                    Resource.Success(response.body())
                }
            } else {
                Resource.Error(errorType = ErrorType.Unknown(response.message()))
            }
        } catch (e: Exception) {
            Resource.Error(errorType = ErrorType.Unknown(e.message))
        }
    }

    @SuppressLint("ApplySharedPref")
    override suspend fun getLocation(): Resource<LatLng> {

        val lat = preferences.getFloat(KEY_LOCATION_LATITUDE, 0f)
        val lng = preferences.getFloat(KEY_LOCATION_LATITUDE, 0f)
        val cachedLocation = LatLng(lat.toDouble(), lng.toDouble())

        if (!LocationUtils.isLocationEnabled(context)) {
            return Resource.Error(cachedLocation, ErrorType.LocationDisabled)
        } else if (!Utils.isInternetAvailable(context)) {
            return Resource.Error(cachedLocation, ErrorType.NoInternet)
        } else if (!PermissionUtils.hasPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            return Resource.Error(cachedLocation, ErrorType.NoLocationPermission)
        }

        try {
            val currentLocation = locationService.getCurrentLocation()
            preferences
                .edit()
                .putFloat(
                    KEY_LOCATION_LATITUDE,
                    currentLocation.latitude.toFloat()
                )
                .putFloat(
                    KEY_LOCATION_LATITUDE,
                    currentLocation.latitude.toFloat()
                )
                .commit()
            return Resource.Success(currentLocation)
        } catch (e: Exception) {
            return Resource.Error(cachedLocation, ErrorType.Unknown(e.message))
        }

    }

}