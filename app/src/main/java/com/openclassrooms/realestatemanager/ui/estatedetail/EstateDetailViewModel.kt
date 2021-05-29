package com.openclassrooms.realestatemanager.ui.estatedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.data.EstateRepository
import com.openclassrooms.realestatemanager.data.MapsRepository
import com.openclassrooms.realestatemanager.data.models.Estate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class EstateDetailViewModel(
    val estateRepository: EstateRepository,
    val mapsRepository: MapsRepository
) : ViewModel() {

    private val _location = MutableLiveData<LatLng>()

    val location: LiveData<LatLng> = _location

    private val _estateId = MutableLiveData<String>()

    val estate = _estateId
        .asFlow()
        .flowOn(Dispatchers.IO)
        .map {
            return@map getEstateById(it)
        }

    private suspend fun getEstateById(id: String): Estate {
        return Estate.DUMMY_ESTATES.find { it.id == id }!!
    }

    fun setEstateId(id: String) {
        _estateId.value = id
    }
}