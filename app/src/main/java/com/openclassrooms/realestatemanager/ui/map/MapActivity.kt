package com.openclassrooms.realestatemanager.ui.map

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.activityresultcontracts.LocationSettingsContract
import com.openclassrooms.realestatemanager.databinding.ActivityMapBinding
import com.openclassrooms.realestatemanager.others.EXTRA_ESTATE_ID
import com.openclassrooms.realestatemanager.others.ErrorType
import com.openclassrooms.realestatemanager.others.Resource
import com.openclassrooms.realestatemanager.others.STATIC_MAP_ZOOM_LEVEL
import com.openclassrooms.realestatemanager.ui.estatedetail.EstateDetailActivity
import com.openclassrooms.realestatemanager.utils.DrawableUtils
import com.openclassrooms.realestatemanager.utils.LocationUtils
import com.openclassrooms.realestatemanager.utils.UiUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapActivity : AppCompatActivity() {

    private lateinit var locationPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var locationSettingsLauncher: ActivityResultLauncher<Void>

    private val viewModel: MapViewModel by viewModel()

    private lateinit var binding: ActivityMapBinding

    private var googleMap: GoogleMap? = null

    private lateinit var houseIcon: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        houseIcon = DrawableUtils.getBitmap(this, R.drawable.ic_house)!!
        Settings.ACTION_LOCATION_SOURCE_SETTINGS
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map)
        binding.viewModel = viewModel
        binding.mapView.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.mapView.getMapAsync {
            googleMap = it
            setupGoogleMap()
        }

        setupLaunchers()

    }

    private fun setupGoogleMap() {
        googleMap?.let {

            setMapStyle()

            it.uiSettings.apply {
                isMyLocationButtonEnabled = false
                isMapToolbarEnabled = false
            }

            viewModel.estates.observe(this) {
                showEstates(it)
            }

            viewModel.location.observe(this) {
                when (it) {
                    is Resource.Error -> {
                        when (it.errorType) {
                            is ErrorType.NoLocationPermission -> locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            is ErrorType.LocationDisabled -> showLocationDisabled()
                        }
                    }
                }

                it?.data?.let { location ->
                    val cameraUpdate =
                        CameraUpdateFactory.newLatLngZoom(location, STATIC_MAP_ZOOM_LEVEL)
                    googleMap?.animateCamera(cameraUpdate)
                }

            }
        }
    }

    private fun setMapStyle() {
        if (UiUtils.isDarkMode(this)) {
            googleMap?.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this,
                    R.raw.night_map_style
                )
            )
        }
    }

    private fun setupLaunchers() {
        setupLocationPermissionLauncher()
        setupLocationSettingsLauncher()
    }

    private fun setupLocationPermissionLauncher() {
        locationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                viewModel.getCurrentLocation()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        showLocationPermissionRational()
                    } else {
                        showLocationPermanentlyDenied()
                    }
                } else {
                    showLocationPermissionRational()
                }
            }
        }
    }

    private fun setupLocationSettingsLauncher() {
        locationSettingsLauncher = registerForActivityResult(
            LocationSettingsContract()
        ) {
            if (LocationUtils.isLocationEnabled(this)) {
                viewModel.getCurrentLocation()
            }
        }
    }

    private fun showLocationDisabled() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.location_disabled_title)
            .setMessage(R.string.location_disabled_message)
            .setPositiveButton(R.string.enable_text) { dialog, which ->
                locationSettingsLauncher.launch(null)
            }
            .setNegativeButton(R.string.no_thanks_text) { dialog, which ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showLocationPermissionRational() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.permission_denied_text)
            .setMessage(R.string.permission_location_rational_text)
            .setPositiveButton(R.string.retry_text) { dialog, which ->
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            .setNegativeButton(R.string.no_thanks_text) { dialog, which ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showLocationPermanentlyDenied() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.permission_denied_text)
            .setMessage(R.string.permission_location_permanently_denied_text)
            .setPositiveButton(R.string.navigate_to_app_settings_text) { dialog, which ->
                navigateToAppSettings()
            }
            .create()
            .show()
    }

    private fun navigateToAppSettings() {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:com.openclassrooms.realestatemanager")
            startActivity(this)
        }
    }

    private fun showEstates(estates: List<Pair<String, LatLng?>>) {
        googleMap?.let {
            it.clear()
            for (estateState in estates) {
                estateState.second?.let { loc ->
                    val marker = it.addMarker(
                        MarkerOptions()
                            .anchor(0.5f, 0.5f)
                            .icon(BitmapDescriptorFactory.fromBitmap(houseIcon))
                            .position(loc)
                    )
                    marker?.tag = estateState.first
                }
            }

            it.setOnMarkerClickListener {
                Intent(this, EstateDetailActivity::class.java).apply {
                    putExtra(EXTRA_ESTATE_ID, it.tag as String)
                    startActivity(this)
                }

                true
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}