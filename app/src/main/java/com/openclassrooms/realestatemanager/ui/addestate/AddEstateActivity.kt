package com.openclassrooms.realestatemanager.ui.addestate

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.activityresultcontracts.AutocompleteActivityResultContract
import com.openclassrooms.realestatemanager.databinding.ActivityAddEstateBinding
import com.openclassrooms.realestatemanager.others.ADD_PHOTO_DIALOG_FRAGMENT_TAG
import com.openclassrooms.realestatemanager.others.EXTRA_ESTATE_ID
import com.openclassrooms.realestatemanager.others.ErrorType
import com.openclassrooms.realestatemanager.others.Resource
import com.openclassrooms.realestatemanager.utils.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AddEstateActivity : AppCompatActivity() {

    private lateinit var autocompleteLauncher: ActivityResultLauncher<Intent>
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var getContentsLauncher: ActivityResultLauncher<String>

    private lateinit var binding: ActivityAddEstateBinding

    private val inputEstateId by lazy {
        intent.getStringExtra(EXTRA_ESTATE_ID)
    }

    private val viewModel: AddEstateViewModel by viewModel {
        parametersOf(inputEstateId)
    }

    private lateinit var estateTypes: List<String>

    private lateinit var adapter: AddEstateImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_estate)

        estateTypes = resources.getStringArray(R.array.estate_type_array).toList()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.inputAddress.setOnClickListener {
            if (Utils.isInternetAvailable(this)) {
                val autocompleteIntent = Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY,
                    listOf(Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS)
                )
                    .setTypeFilter(TypeFilter.ADDRESS)
                    .setHint(binding.inputAddress.editText!!.text.toString())
                    .build(this)
                autocompleteLauncher.launch(autocompleteIntent)
            } else {
                binding.inputAddress.editText!!.requestFocus()
            }
        }

        setupAddPhoto()
        setupLaunchers()
        setupAdapter()
        setupSpinner()
        setupObservers()
    }

    private fun setupAddPhoto() {
        binding.btnAddImage.setOnClickListener {
            getContentsLauncher.launch("image/*")
        }
    }

    private fun setupLaunchers() {
        autocompleteLauncher = registerForActivityResult(
            AutocompleteActivityResultContract()
        ) {
            it?.let {
                viewModel.onAddressPlaceReceive(it)
                binding.inputAddress.editText?.setText(it.address)
            }
        }

        takePictureLauncher = registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ) {

        }

        getContentsLauncher = registerForActivityResult(
            ActivityResultContracts.GetMultipleContents()
        ) {
            viewModel.onPhotoReceive(it)
        }

    }

    private fun setupAdapter() {
        adapter = AddEstateImageAdapter {
            viewModel.onEstateImageClick(it)
        }
        binding.rvImages.adapter = adapter
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.estate_type_array,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerType.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.estate.observe(this) {
            binding.estate = it.data

            it.data?.saleDateTs?.let {
                binding.toggleState.check(R.id.btnSold)
                binding.btnSold.isEnabled = false
            }

            binding.spinnerType.setSelection(
                estateTypes.indexOf(it.data?.type)
            )
        }

        viewModel.estateImages.observe(this) {
            adapter.submitList(it.data)
        }

        viewModel.editingImage.observe(this) {
            val photoDialog = supportFragmentManager.findFragmentByTag(ADD_PHOTO_DIALOG_FRAGMENT_TAG) as AddPhotoDialogFragment? ?: AddPhotoDialogFragment()
            if (!photoDialog.isVisible && !photoDialog.isAdded) {
                photoDialog.show(supportFragmentManager, ADD_PHOTO_DIALOG_FRAGMENT_TAG)
            }
        }

        viewModel.uploadState.observe(this) {
            when (it) {
                is Resource.Success -> finish()
                is Resource.Error -> {
                    if (it.errorType is ErrorType.NoInternet) {
                        Toast.makeText(this, R.string.edit_estate_offline_error_text, Toast.LENGTH_LONG).show()
                    }

                    finish()
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.estate_add_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_check -> {
                viewModel.onConfirmClick(
                    binding.inputAddress.editText!!.text.toString(),
                    binding.inputDescription.editText!!.text.toString(),
                    binding.spinnerType.selectedItemPosition.let { estateTypes[it] },
                    binding.inputSurface.editText!!.text.toString().toFloat(),
                    binding.inputRoomCount.editText!!.text.toString().toInt(),
                    binding.inputBathroomCount.editText!!.text.toString().toInt(),
                    binding.inputBedroomCount.editText!!.text.toString().toInt(),
                    binding.inputPrice.editText!!.text.toString().toLong(),
                    binding.toggleState.checkedButtonIds.contains(R.id.btnSold)
                )

                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

}