package com.openclassrooms.realestatemanager.ui.estatelist

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.DialogFragmentFilterBinding
import com.openclassrooms.realestatemanager.others.FILTER_TYPES
import com.openclassrooms.realestatemanager.others.KEY_FILTER_DATA
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FilterDialogFragment : DialogFragment() {

    private val viewModel: EstateListViewModel by sharedViewModel()

    private lateinit var binding: DialogFragmentFilterBinding

    private val btnIds =
        listOf(R.id.btnSchool, R.id.btnStore, R.id.btnRestaurant, R.id.btnTrain, R.id.btnAirport)

    private lateinit var typesList: List<String>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogFragmentFilterBinding.inflate(layoutInflater)

        typesList = resources.getStringArray(R.array.estate_type_filter_array).toList()

        setupInitValues()
        setupSliderListeners()
        (requireArguments().getSerializable(KEY_FILTER_DATA) as? EstateFilterData)?.let { setupUi(it) }

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.filter_title)
            .setView(binding.root)
            .setPositiveButton(R.string.done, null)
            .setNeutralButton(R.string.cancel, null)
            .setNegativeButton(R.string.reset, null)
            .create()
    }

    private fun setupInitValues() {
        binding.apply {
            tvPriceFrom.text = getString(R.string.price, 0)
            tvPriceTo.text =
                getString(R.string.price, resources.getInteger(R.integer.slider_price_max_value))

            tvSurfaceFrom.text = getString(R.string.surface, 0)
            tvSurfaceTo.text = getString(
                R.string.surface,
                resources.getInteger(R.integer.slider_surface_max_value)
            )

            spinnerType.adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.estate_type_filter_array,
                android.R.layout.simple_spinner_item
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

            spinnerPhotoCount.adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.filter_photo_count_array,
                android.R.layout.simple_spinner_item
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        }
    }

    private fun setupUi(estateFilterData: EstateFilterData) {
        estateFilterData.apply {
            binding.rangeSliderPrice.setValues(
                priceRange.first.toFloat(),
                priceRange.last.toFloat()
            )
            binding.rangeSliderSurface.setValues(
                surfaceRange.first.toFloat(),
                surfaceRange.last.toFloat()
            )

            setPriceRange(priceRange.first, priceRange.last)
            setSurfaceRange(surfaceRange.first, surfaceRange.last)

            for (type in nearTypes) {
                binding.groupTypes.check(btnIds[FILTER_TYPES.indexOf(type)])
            }

            type?.let { type ->
                binding.spinnerType.setSelection(typesList.indexOf(type))
            }

            photoCount?.let { count ->
                binding.spinnerPhotoCount.setSelection(count + 1)
            }

            isForSale?.let { forSale ->
                binding.groupState.check(if (forSale) R.id.btnForSale else R.id.btnSold)
            }

        }
    }

    private fun setupSliderListeners() {
        binding.apply {
            rangeSliderPrice.addOnChangeListener { slider, value, fromUser ->
                setPriceRange(slider.values[0].toLong(), slider.values[1].toLong())
            }

            rangeSliderSurface.addOnChangeListener { slider, value, fromUser ->
                setSurfaceRange(slider.values[0].toInt(), slider.values[1].toInt())
            }
        }
    }

    override fun onStart() {
        super.onStart()

        (dialog as AlertDialog).apply {
            getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
                sendFilterData()
            }

            getButton(AlertDialog.BUTTON_NEGATIVE)?.setOnClickListener {
                binding.apply {
                    rangeSliderPrice.apply {
                        setValues(valueFrom, valueTo)
                    }

                    rangeSliderSurface.apply {
                        setValues(valueFrom, valueTo)
                    }

                    spinnerType.setSelection(0)

                    groupTypes.clearChecked()

                    spinnerPhotoCount.setSelection(0)

                    groupState.clearChecked()
                }

            }
        }

    }

    private fun sendFilterData() {
        EstateFilterData(
            LongRange(
                binding.rangeSliderPrice.values[0].toLong(),
                binding.rangeSliderPrice.values[1].toLong()
            ),
            binding.groupTypes.checkedButtonIds.map { FILTER_TYPES[btnIds.indexOf(it)] },
            IntRange(
                binding.rangeSliderSurface.values[0].toInt(),
                binding.rangeSliderSurface.values[1].toInt()
            ),
            binding.spinnerType.takeIf { it.selectedItemPosition != 0 }
                ?.let { typesList[it.selectedItemPosition] },
            binding.spinnerPhotoCount.selectedItemPosition.takeIf { it != 0 }?.minus(1),
            binding.groupState.checkedButtonId.takeIf { it != View.NO_ID }
                ?.let { it == R.id.btnForSale }
        ).also {
            dismiss()
            viewModel.setFilter(it)
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