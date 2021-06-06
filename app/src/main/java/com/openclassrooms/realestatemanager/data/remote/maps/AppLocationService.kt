package com.openclassrooms.realestatemanager.data.remote.maps

import android.annotation.SuppressLint
import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AppLocationService(context: Context) : LocationService {

    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val criteria = Criteria()

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): LatLng = suspendCancellableCoroutine { cont ->

        val provider = locationManager.getBestProvider(criteria, true)
            ?: throw Exception("No location provider")

        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                cont.resume(LatLng(location.latitude, location.longitude))
            }

            override fun onProviderDisabled(provider: String) {

            }

            override fun onProviderEnabled(provider: String) {

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

            }
        }

        locationManager.requestSingleUpdate(provider, locationListener, Looper.getMainLooper())

        cont.invokeOnCancellation {
            locationManager.removeUpdates(locationListener)
        }
    }

}