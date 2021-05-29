package com.openclassrooms.realestatemanager.data.remote.maps

import android.annotation.SuppressLint
import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.CancellationSignal
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.Executors

class AppLocationService(
    val context: Context
) : LocationService {

    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val provider = locationManager.getBestProvider(Criteria(), true)

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): LatLng {

        provider ?: throw Exception("Location provider is missing")

        return if (Build.VERSION.SDK_INT >= 30) {
            suspendCancellableCoroutine {

                val cancellationSignal = CancellationSignal()

                locationManager.getCurrentLocation(
                    provider,
                    cancellationSignal,
                    Executors.newSingleThreadExecutor()
                ) { location ->
                    it.resumeWith(Result.success(LatLng(location.latitude, location.longitude)))
                }

                it.invokeOnCancellation {
                    cancellationSignal.cancel()
                }
            }
        } else {
            suspendCancellableCoroutine {

                val locationListener: ((Location) -> Unit) = { location ->
                    it.resumeWith(Result.success(LatLng(location.latitude, location.longitude)))
                }

                locationManager.requestSingleUpdate(provider, locationListener, null)

                it.invokeOnCancellation {
                    locationManager.removeUpdates(locationListener)
                }
            }
        }

    }

}