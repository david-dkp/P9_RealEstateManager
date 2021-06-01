package com.openclassrooms.realestatemanager.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.data.EstateRepository
import com.openclassrooms.realestatemanager.data.MapsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MapViewModel(
    estateRepository: EstateRepository,
    mapsRepository: MapsRepository
) : ViewModel() {

    val estates = liveData(Dispatchers.IO) {
        estateRepository.getEstatesFlow().collect {
            emit(
                it.map { estate ->
                    estate.id to mapsRepository.getGeocodingResult(estate.address!!).data?.geometry?.location?.toLatLng()
                }
            )
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            estateRepository.refreshEstates()
        }
    }

}