package com.openclassrooms.realestatemanager.data

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.data.models.responses.GeocodingResponse
import com.openclassrooms.realestatemanager.data.models.responses.NearbySearchResponse
import com.openclassrooms.realestatemanager.data.remote.maps.GeocodingCache
import com.openclassrooms.realestatemanager.data.remote.maps.MapsApi
import com.openclassrooms.realestatemanager.data.remote.maps.NearbySearchCache
import com.openclassrooms.realestatemanager.others.ErrorType
import com.openclassrooms.realestatemanager.others.Resource
import com.openclassrooms.realestatemanager.others.NEARBY_SEARCH_RADIUS
import com.openclassrooms.realestatemanager.utils.Utils
import java.util.*

class AppMapsRepository(
    val context: Context,
    val mapsApi: MapsApi,
    val geocodingCache: GeocodingCache,
    val nearbySearchCache: NearbySearchCache
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
            e.printStackTrace()
            Resource.Error(errorType = ErrorType.Unknown(e.message))
        }
    }

    override suspend fun getNearbyResults(location: LatLng): Resource<List<NearbySearchResponse.Result>> {
        val cachedNearbySearch = nearbySearchCache.get(location)

        if (!Utils.isInternetAvailable(context)) {
            return Resource.Error(cachedNearbySearch?.results, errorType = ErrorType.NoInternet)
        }

        return cachedNearbySearch?.results?.let {
            Resource.Success(it)
        } ?: try {
            val response = mapsApi.getNearbySearch(
                "${location.latitude},${location.longitude}",
                NEARBY_SEARCH_RADIUS,
                BuildConfig.MAPS_API_KEY
            )

            if (response.isSuccessful) {
                if (response.body()!!.results.isEmpty()) {
                    nearbySearchCache.put(location, response.body()!!)
                    Resource.Error(errorType = ErrorType.CantFoundAddress)
                } else {
                    nearbySearchCache.put(location, response.body()!!)
                    Resource.Success(response.body()!!.results)
                }
            } else {
                Resource.Error(errorType = ErrorType.Unknown(response.message()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(errorType = ErrorType.Unknown(e.message))
        }
    }

}