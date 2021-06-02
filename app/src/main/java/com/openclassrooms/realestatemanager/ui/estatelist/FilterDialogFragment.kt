package com.openclassrooms.realestatemanager.ui.estatelist

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.load.engine.bitmap_recycle.IntegerArrayAdapter
import com.google.android.libraries.places.api.model.Place
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.DialogFragmentFilterBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FilterDialogFragment : DialogFragment() {

    private val viewModel: EstateListViewModel by sharedViewModel()

    private lateinit var binding: DialogFragmentFilterBinding

    private val types = listOf(Place.Type.SCHOOL, Place.Type.STORE, Place.Type.RESTAURANT, Place.Type.TRAIN_STATION, Place.Type.AIRPORT)
    private val btnIds = listOf(R.id.btnSchool, R.id.btnStore, R.id.btnRestaurant, R.id.btnTrain, R.id.btnAirport)

    private var firstRun: Boolean = true

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        firstRun = savedInstanceState == null

        binding = DialogFragmentFilterBinding.inflate(layoutInflater)

        binding.spinnerPhotoCount.adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.filter_photo_count_array,
            android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.rangeSliderPrice.addOnChangeListener { slider, value, fromUser ->
            setPriceRange(slider.values[0].toLong(), slider.values[1].toLong())
        }

        binding.rangeSliderSurface.addOnChangeListener { slider, value, fromUser ->
            setSurfaceRange(slider.values[0].toInt(), slider.values[1].toInt())
        }

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.filter_title)
            .setView(binding.root)
            .setPositiveButton(R.string.done, null)
            .setNeutralButton(R.string.cancel, null)
            .setNegativeButton(R.string.reset, null)
            .create()
    }

    override fun onStart() {
        super.onStart()
        viewModel.filterData.observe(requireActivity()) {

            if (!firstRun) return@observe

            binding.filterData = it

            binding.rangeSliderPrice.setValues(it.priceRange.first.toFloat(), it.priceRange.second.toFloat())
            binding.rangeSliderSurface.setValues(it.surfaceRange.first.toFloat(), it.surfaceRange.second.toFloat())

            setPriceRange(it.priceRange.first, it.priceRange.second)
            setSurfaceRange(it.surfaceRange.first, it.surfaceRange.second)

            for (type in it.nearTypes) {
                binding.groupTypes.check(btnIds[types.indexOf(type)])
            }

            it.photoCount?.let { count ->
                binding.spinnerPhotoCount.setSelection(count+1)
            }

            it.isForSale?.let { forSale ->
                binding.groupState.check(if (forSale) R.id.btnForSale else R.id.btnSold)
            }

        }
    }

    private fun setPriceRange(fromPrice: Long, toPrice: Long) {
        binding.tvPriceFrom.text = getString(R.string.price, fromPrice)
        binding.tvPriceTo.text = getString(R.string.price, toPrice)
    }

    private fun setSurfaceRange(fromSurface: Int, toSurface: Int) {
        binding.tvSurfaceFrom.text = getString(R.string.surface, fromSurface)
        binding.tvSurfaceTo.text = getString(R.string.surface, toSurface)
    }

}