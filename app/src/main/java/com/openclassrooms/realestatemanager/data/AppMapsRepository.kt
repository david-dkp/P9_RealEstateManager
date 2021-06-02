package com.openclassrooms.realestatemanager.data

import android.content.Context
import androidx.preference.PreferenceManager
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.data.models.dto.GeocodingResponse
import com.openclassrooms.realestatemanager.data.models.dto.TextSearchResponse
import com.openclassrooms.realestatemanager.data.remote.maps.GeocodingCache
import com.openclassrooms.realestatemanager.data.remote.maps.MapsApi
import com.openclassrooms.realestatemanager.data.remote.maps.TextSearchCache
import com.openclassrooms.realestatemanager.others.ErrorType
import com.openclassrooms.realestatemanager.others.Resource
import com.openclassrooms.realestatemanager.utils.Utils
import java.util.*

class AppMapsRepository(
    val context: Context,
    val mapsApi: MapsApi,
    val geocodingCache: GeocodingCache,
    val textSearchCache: TextSearchCache
) : MapsRepository {

    override suspend fun getGeocodingResult(address: String): Resource<GeocodingResponse.Result> {

        val cachedGeocoding = geocodingCache.get(address)

        if (!Utils.isInternetAvailable(context)) {
            return Resource.Error(cachedGeocoding?.results?.first(), errorType = ErrorType.NoInternet)
        }

        return cachedGeocoding?.results?.first()?.let {
            Resource.Success(it)
        } ?: try {
            val response = mapsApi.getGeocoding(
                address,
                Locale.getDefault().language,
                BuildConfig.MAPS_API_KEY
            )

            if (response.isSuccessful) {
                if (response.body()!!.results.isEmpty()) {
                    geocodingCache.put(address, response.body()!!)
                    Resource.Error(errorType = ErrorType.CantFoundAddress)
                } else {
                    geocodingCache.put(address, response.body()!!)
                    Resource.Success(response.body()!!.results.first())
                }
            } else {
                Resource.Error(errorType = ErrorType.Unknown(response.message()))
            }
        } catch (e: Exception) {
            Resource.Error(errorType = ErrorType.Unknown(e.message))
        }
    }

    override suspend fun getTextSearchResults(address: String): Resource<List<TextSearchResponse.Result>> {
        val cachedTextSearch = textSearchCache.get(address)

        if (!Utils.isInternetAvailable(context)) {
            return Resource.Error(cachedTextSearch?.results, errorType = ErrorType.NoInternet)
        }

        return cachedTextSearch?.results?.let {
            Resource.Success(it)
        } ?: try {
            val response = mapsApi.getTextSearch(
                address,
                BuildConfig.MAPS_API_KEY
            )

            if (response.isSuccessful) {
                if (response.body()!!.results.isEmpty()) {
                    textSearchCache.put(address, response.body()!!)
                    Resource.Error(errorType = ErrorType.CantFoundAddress)
                } else {
                    textSearchCache.put(address, response.body()!!)
                    Resource.Success(response.body()!!.results)
                }
            } else {
                Resource.Error(errorType = ErrorType.Unknown(response.message()))
            }
        } catch (e: Exception) {
            Resource.Error(errorType = ErrorType.Unknown(e.message))
        }
    }

}