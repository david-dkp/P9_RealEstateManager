package com.openclassrooms.realestatemanager.ui.estatedetail

import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.data.EstateRepository
import com.openclassrooms.realestatemanager.data.MapsRepository
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.models.EstateImage
import com.openclassrooms.realestatemanager.data.models.GeocodingResponse
import com.openclassrooms.realestatemanager.others.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class EstateDetailViewModel(
    val estateRepository: EstateRepository,
    val mapsRepository: MapsRepository
) : ViewModel() {

    private val _estateId = MutableLiveData<String>()
    val estateId: LiveData<String> = _estateId

    private val _estateIdFlow = _estateId
        .asFlow()
        .shareIn(viewModelScope, SharingStarted.Eagerly, 1)

    val estate = liveData(Dispatchers.IO) {
        _estateIdFlow.collect {
            emit(Resource.Loading())
            emit(estateRepository.getEstateById(it))
        }
    }

    val estateImages: LiveData<Resource<List<EstateImage>>> = liveData(Dispatchers.IO){
        _estateIdFlow.collect {
            emit(Resource.Loading())
            emit(estateRepository.getEstateImagesByEstateId(it))
        }
    }

    val location: LiveData<Resource<LatLng>> = liveData (Dispatchers.IO){
        estate
            .asFlow()
            .collect {
                if (it is Resource.Success) {
                    emit(Resource.Loading())
                    val resource = mapsRepository.getGeocodingResult(it.data?.address!!)

                    emit(
                        when (resource){
                            is Resource.Success -> Resource.Success(resource.data?.geometry?.location?.toLatLng())
                            is Resource.Error -> Resource.Error(resource.data?.geometry?.location?.toLatLng(), resource.errorType!!)
                            else -> Resource.Error()
                        }
                    )

                }
            }
    }


    fun setEstateId(id: String) {
        _estateId.value = id
    }
}