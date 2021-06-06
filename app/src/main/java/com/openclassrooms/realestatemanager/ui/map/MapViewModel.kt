package com.openclassrooms.realestatemanager.ui.map

import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.data.EstateRepository
import com.openclassrooms.realestatemanager.data.MapsRepository
import com.openclassrooms.realestatemanager.others.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MapViewModel(
    val estateRepository: EstateRepository,
    val mapsRepository: MapsRepository
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

    private val _location = MutableLiveData<Resource<LatLng>>()
    val location: LiveData<Resource<LatLng>> = _location

    init {
        viewModelScope.launch(Dispatchers.IO) {
            estateRepository.refreshEstates()
        }

        getCurrentLocation()
    }

    fun getCurrentLocation() {
        _location.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            val resource = mapsRepository.getCurrentLocation()
            _location.postValue(resource)
        }
    }

}