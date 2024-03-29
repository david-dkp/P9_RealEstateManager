package com.openclassrooms.realestatemanager.apis

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.google.android.gms.maps.model.LatLng
import com.google.gson.GsonBuilder
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.data.remote.maps.MapsApi
import com.openclassrooms.realestatemanager.others.MAPS_API_BASE_URL
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(AndroidJUnit4::class)
@MediumTest
class MapsApiTest {

    lateinit var mapsApi: MapsApi

    @Before
    fun setup() {
        mapsApi = Retrofit.Builder()
            .baseUrl(MAPS_API_BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()
                        .setLenient()
                        .create()
                )
            )
            .build()
            .create(MapsApi::class.java)
    }

    @Test
    fun getPositionFromGeocoding() {
        runBlocking {
            val response =
                mapsApi.getGeocoding("335 rue des pralets", "fr", BuildConfig.MAPS_API_KEY)
            val latLng = response.body()!!.results.first().geometry.location.toLatLng()

            assert(latLng == LatLng(46.283665, 6.084062))
        }
    }

    @Test
    fun getNearbyTypesFromTextSearch() {
        runBlocking {
            val response =
                mapsApi.getNearbySearch("46.283664,6.084062", 3000, BuildConfig.MAPS_API_KEY)
            Log.d("debug", "response : ${response.body()!!}")

            val types = response.body()!!.results.flatMap {
                it.types
            }
            Log.d("debug", "types: $types")
        }
    }

}