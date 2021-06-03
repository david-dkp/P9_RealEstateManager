package com.openclassrooms.realestatemanager.ui.estatelist

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.google.android.libraries.places.api.model.Place
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.data.EstateRepository
import com.openclassrooms.realestatemanager.data.MapsRepository
import com.openclassrooms.realestatemanager.data.UserRepository
import com.openclassrooms.realestatemanager.data.models.domain.Estate
import com.openclassrooms.realestatemanager.data.models.responses.NearbySearchResponse
import com.openclassrooms.realestatemanager.others.Resource
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.util.*

class EstateListViewModel(
    val context: Context,
    val estateRepository: EstateRepository,
    val userRepository: UserRepository,
    val mapsRepository: MapsRepository
) : ViewModel() {

    private val auth = Firebase.auth

    @ExperimentalCoroutinesApi
    val isLoggedIn = callbackFlow {

        val authListener: ((FirebaseAuth?) -> Unit) = {
            trySend(it != null && it.currentUser != null)
        }

        auth.addAuthStateListener(authListener)

        awaitClose { auth.removeAuthStateListener(authListener) }
    }

    val user = liveData {
        emit(
            userRepository.getCurrentUser()
        )
    }

    private val _filterData = MutableLiveData<EstateFilterData>()
    val filterData: LiveData<EstateFilterData> = _filterData

    private val _repoEstates = estateRepository.getEstatesFlow().asLiveData()

    private val _estates = MediatorLiveData<List<Estate>>().apply {
        addSource(_repoEstates) {
            updateEstates(it, filterData.value)
        }

        addSource(_filterData) { filterData ->
            _repoEstates.value?.let {
                updateEstates(it, filterData)
            }
        }
    }

    val estates: LiveData<List<Estate>> = _estates

    private val _refreshState = MutableLiveData<Resource<Void>>()
    val refreshState: LiveData<Resource<Void>> = _refreshState


    init {
        viewModelScope.launch {
            estateRepository.refreshEstates()
        }
    }

    private fun updateEstates(
        estates: List<Estate>,
        filterData: EstateFilterData?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            var filteredList = estates

            filterData?.let {
                filteredList = filteredList.filter { estate ->
                    filterData.priceRange.contains(estate.price)
                            && filterData.surfaceRange.contains(estate.surfaceArea)
                            && filterData.photoCount?.let { filterData.photoCount == estate.photoCount } ?: true
                            && filterData.isForSale?.let { it == (estate.saleDateTs == null) } ?: true
                }

                if (filterData.nearTypes.isNotEmpty()) {
                    filteredList = filterEstatesByNearTypes(filteredList, filterData.nearTypes)
                }
            }

            _estates.postValue(filteredList)
        }

    }

    private suspend fun filterEstatesByNearTypes(estates: List<Estate>, types: List<Place.Type>) =
        coroutineScope {
            val estatesNearTypes = estates
                .map {
                    async {
                        val geocodingResult =
                            mapsRepository.getGeocodingResult(it.address!!)
                        mapsRepository.getNearbyResults(geocodingResult.data!!.geometry.location.toLatLng())
                    }
                }
                .awaitAll()
                .filterIsInstance<Resource.Success<List<NearbySearchResponse.Result>>>()
                .map {
                    it.data!!.flatMap { results ->
                        results.types
                    }
                }

            val filterTypesString = types.map {
                it.toString()
                    .lowercase(Locale.getDefault())
            }

            return@coroutineScope estates.filterIndexed { index, estate ->
                estatesNearTypes[index].containsAll(filterTypesString)
            }
        }

    fun logout() {
        auth.signOut()
    }

    fun refreshEstates() {
        _refreshState.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {

            _refreshState.postValue(estateRepository.refreshEstates())

        }
    }

    fun setFilter(estateFilterData: EstateFilterData) {
        _filterData.value = estateFilterData
    }
}