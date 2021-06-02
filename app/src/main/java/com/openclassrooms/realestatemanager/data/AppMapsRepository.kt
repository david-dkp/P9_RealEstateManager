package com.openclassrooms.realestatemanager.data

import android.content.Context
import androidx.preference.PreferenceManager
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.data.models.dto.GeocodingResponse
import com.openclassrooms.realestatemanager.data.remote.maps.MapsApi
import com.openclassrooms.realestatemanager.others.ErrorType
import com.openclassrooms.realestatemanager.others.Resource
import com.openclassrooms.realestatemanager.utils.Utils
import java.util.*

class AppMapsRepository(
    val context: Context,
    val mapsApi: MapsApi,
) : MapsRepository {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    override suspend fun getGeocodingResult(address: String): Resource<GeocodingResponse.Result> {

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
                    Resource.Success(response.body()!!.results.first())
                }
            } else {
                Resource.Error(errorType = ErrorType.Unknown(response.message()))
            }
        } catch (e: Exception) {
            Resource.Error(errorType = ErrorType.Unknown(e.message))
        }
    }

}