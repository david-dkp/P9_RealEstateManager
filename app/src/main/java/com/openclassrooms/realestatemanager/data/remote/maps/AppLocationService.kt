package com.openclassrooms.realestatemanager.data.remote.maps

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AppLocationService(context: Context) : LocationService {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): LatLng  = suspendCoroutine {

        val locationCallback = object: LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.lastLocation?.let { location ->
                    it.resume(LatLng(location.latitude, location.longitude))
                } ?: it.resumeWithException(Exception("Something went wrong getting the current position"))
            }
        }

        fusedLocationClient.requestLocationUpdates(LocationRequest.create(), locationCallback, Looper.myLooper())

    }

}