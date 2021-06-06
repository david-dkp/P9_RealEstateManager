package com.openclassrooms.realestatemanager.ui.map

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMapBinding
import com.openclassrooms.realestatemanager.others.EXTRA_ESTATE_ID
import com.openclassrooms.realestatemanager.others.STATIC_MAP_ZOOM_LEVEL
import com.openclassrooms.realestatemanager.ui.estatedetail.EstateDetailActivity
import com.openclassrooms.realestatemanager.utils.DrawableUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapActivity : AppCompatActivity() {

    private val viewModel: MapViewModel by viewModel()

    private lateinit var binding: ActivityMapBinding

    private var googleMap: GoogleMap? = null

    private lateinit var houseIcon: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        houseIcon = DrawableUtils.getBitmap(this, R.drawable.ic_house)!!

        binding = DataBindingUtil.setContentView(this, R.layout.activity_map)
        binding.viewModel = viewModel
        binding.mapView.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.mapView.getMapAsync {
            googleMap = it
            setupGoogleMap()
        }

    }

    private fun setupGoogleMap() {
        googleMap?.let {
            it.uiSettings.apply {
                isMyLocationButtonEnabled = false
                isMapToolbarEnabled = false
            }

            viewModel.estates.observe(this) {
                showEstates(it)
            }

            viewModel.location.observe(this) {
                it?.data?.let { location ->
                    val cameraUpdate =
                        CameraUpdateFactory.newLatLngZoom(location, STATIC_MAP_ZOOM_LEVEL)
                    googleMap?.animateCamera(cameraUpdate)
                }
            }

            viewModel.getCurrentLocation()
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