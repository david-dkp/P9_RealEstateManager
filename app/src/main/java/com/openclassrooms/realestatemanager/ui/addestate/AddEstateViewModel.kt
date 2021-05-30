package com.openclassrooms.realestatemanager.ui.addestate

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.openclassrooms.realestatemanager.data.EstateRepository
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.models.EstateImage
import com.openclassrooms.realestatemanager.others.Resource
import com.openclassrooms.realestatemanager.utils.IdUtils

class AddEstateViewModel(
    val inputEstateId: String?,
    val estateRepository: EstateRepository
) : ViewModel() {

    val estateId = inputEstateId ?: IdUtils.generateId(20)

    val estate = liveData {
        estateId?.let {
            emit(Resource.Loading())
            emit(estateRepository.getEstateById(estateId))
        }
    }

    val estateImages = liveData {
        estateId?.let {
            emit(Resource.Loading())
            emit(estateRepository.getEstateImagesByEstateId(estateId))
        }
    }

    val _editingImages = arrayListOf<EstateImage>()

    val editingImages: List<EstateImage> = _editingImages

    fun onPhoto(uris: List<Uri>) {
        _editingImages.addAll(uris.map { EstateImage(
            IdUtils.generateId(20),
            "",
            estateId,
            "",
            it.toString()
        ) })
    }

}