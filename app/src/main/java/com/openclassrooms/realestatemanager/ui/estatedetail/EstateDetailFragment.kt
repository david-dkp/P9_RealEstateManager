package com.openclassrooms.realestatemanager.ui.estatedetail

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentEstateDetailBinding
import com.openclassrooms.realestatemanager.others.STATIC_MAP_ZOOM_LEVEL
import com.openclassrooms.realestatemanager.utils.DrawableUtils
import com.openclassrooms.realestatemanager.utils.UiUtils
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EstateDetailFragment : Fragment() {

    private lateinit var binding: FragmentEstateDetailBinding

    private val viewModel: EstateDetailViewModel by sharedViewModel()

    private var googleMap: GoogleMap? = null

    private lateinit var houseIcon: Bitmap

    private val adapter = EstateImageAdapter ()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        houseIcon = DrawableUtils.getBitmap(requireContext(), R.drawable.ic_house)!!

        binding = FragmentEstateDetailBinding.inflate(layoutInflater)

        binding.mapView.onCreate(savedInstanceState)

        binding.rvEstateImages.adapter = adapter

        viewModel.estate.observe(viewLifecycleOwner) {
            binding.estate = it.data
        }

        viewModel.estateImages.observe(viewLifecycleOwner) {
            adapter.submitList(it.data)
        }

        viewModel.location.observe(viewLifecycleOwner) {
            it.data?.let { pos ->
                moveCamera(pos)
            }
        }

        binding.mapView.getMapAsync {
            googleMap = it
            configureGoogleMap()
        }

        return binding.root
    }

    private fun configureGoogleMap() {

        setMapStyle()

        googleMap?.uiSettings?.isMapToolbarEnabled = false

        viewModel.location.value?.let { resource ->
            resource.data?.let {
                moveCamera(it)
            }
        }
    }

    private fun setMapStyle() {
        if (UiUtils.isDarkMode(requireContext())) {
            googleMap?.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.night_map_style
                )
            )
        }
    }

    private fun moveCamera(latLng: LatLng) {
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, STATIC_MAP_ZOOM_LEVEL))
        googleMap?.clear()
        googleMap?.addMarker(
            MarkerOptions()
                .anchor(0.5f, 0.5f)
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(houseIcon))
        )
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

}