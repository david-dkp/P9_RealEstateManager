package com.openclassrooms.realestatemanager.data

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.STATIC_MAP_ZOOM_LEVEL
import com.openclassrooms.realestatemanager.data.remote.maps.MapsApi
import com.openclassrooms.realestatemanager.others.ErrorType
import com.openclassrooms.realestatemanager.others.Resource
import com.openclassrooms.realestatemanager.utils.Utils
import java.lang.Exception
import java.util.*

class AppMapsRepository(
    val context: Context,
    val mapsApi: MapsApi
) : MapsRepository {

    override suspend fun getPositionFromAddress(address: String): Resource<LatLng> {

        if (!Utils.isInternetAvailable(context)) {
            return Resource.Error(errorType = ErrorType.NoInternet)
        }

        return try {
            val response = mapsApi.getGeocoding(address, Locale.getDefault().language, BuildConfig.MAPS_API_KEY)

            if (response.isSuccessful) {
                if (response.body()!!.results.isEmpty()) {
                    Resource.Success(null)
                } else {
                    Resource.Success(response.body()!!.results[0].geometry.location.toLatLng())
                }
            } else {
                Resource.Error(errorType = ErrorType.Unknown(response.message()))
            }
        } catch (e: Exception) {
            Resource.Error(errorType = ErrorType.Unknown(e.message))
        }
    }

    override suspend fun getMapImageUrlFromAddress(address: String): Resource<String> {
        if (!Utils.isInternetAvailable(context)) {
            return Resource.Error(errorType = ErrorType.NoInternet)
        }

        return try {
            val response = mapsApi.getStaticMapImage(address, STATIC_MAP_ZOOM_LEVEL, BuildConfig.MAPS_API_KEY)

            if (response.isSuccessful) {
                Resource.Success(response.body())
            } else {
                Resource.Error(errorType = ErrorType.Unknown(response.message()))
            }

        } catch (e: Exception) {
            Resource.Error(errorType = ErrorType.Unknown(e.message))
        }
    }
}