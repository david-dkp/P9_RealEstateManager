package com.openclassrooms.realestatemanager.apis

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.MAPS_API_BASE_URL
import com.openclassrooms.realestatemanager.STATIC_MAP_ZOOM_LEVEL
import com.openclassrooms.realestatemanager.data.remote.maps.MapsApi
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
                GsonConverterFactory.create(GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .setLenient()
                    .create())
            )
            .build()
            .create(MapsApi::class.java)
    }

    @Test
    fun getPositionFromGeocoding() {
        runBlocking {
            val response = mapsApi.getGeocoding("335 rue des pralets", "fr", BuildConfig.MAPS_API_KEY)
            val latLng = response.body()!!.results.first().geometry.location.toLatLng()

            assert(latLng == LatLng(46.283665, 6.084062))
        }
    }

}