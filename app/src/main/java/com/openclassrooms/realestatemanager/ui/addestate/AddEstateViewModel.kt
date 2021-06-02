package com.openclassrooms.realestatemanager.ui.addestate

import android.content.Context
import android.net.Uri
import androidx.lifecycle.*
import androidx.work.*
import com.google.android.libraries.places.api.model.Place
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.data.EstateRepository
import com.openclassrooms.realestatemanager.data.models.domain.Estate
import com.openclassrooms.realestatemanager.data.models.domain.EstateImage
import com.openclassrooms.realestatemanager.others.ErrorType
import com.openclassrooms.realestatemanager.others.Resource
import com.openclassrooms.realestatemanager.others.SYNC_WORKER_TAG
import com.openclassrooms.realestatemanager.utils.IdUtils
import com.openclassrooms.realestatemanager.utils.LocalityUtils
import com.openclassrooms.realestatemanager.workers.SyncWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class AddEstateViewModel(
    val context: Context,
    val inputEstateId: String?,
    val estateRepository: EstateRepository
) : ViewModel() {

    val estateId = inputEstateId ?: IdUtils.generateId(20)

    val estate = liveData {
        inputEstateId.let {
            emit(Resource.Loading())
            emit(estateRepository.getEstateById(estateId))
        }
    }

    private val _uploadState = MutableLiveData<Resource<Void>>()
    val uploadState: LiveData<Resource<Void>> = _uploadState

    private var _addressPlace: Place? = null

    private val _estateImages = MediatorLiveData<Resource<List<EstateImage>>>()
    val estateImages: LiveData<Resource<List<EstateImage>>> = _estateImages

    init {
        _estateImages.addSource(estate) {
            viewModelScope.launch(Dispatchers.IO) {
                _estateImages.postValue(estateRepository.getEstateImagesByEstateId(estateId))
            }
        }
    }

    private val _editingImage = MutableLiveData<EstateImage?>()
    val editingImage: LiveData<EstateImage?> = _editingImage

    private var _iterator: Iterator<EstateImage>? = null

    fun onPhotoReceive(uris: List<Uri>) {
        val addedEtatesImages = uris.map {
            EstateImage(
                IdUtils.generateId(20),
                "",
                estateId,
                null,
                it.toString()
            )
        }

        _iterator = addedEtatesImages.iterator()

        moveToNextEstateImage()
    }

    fun onAddressPlaceReceive(place: Place) {
        _addressPlace = place
    }

    fun onEditPhoto(description: String) {
        val currentImages = estateImages.value?.data?.toMutableList() ?: arrayListOf()

        _editingImage.value!!.description = description

        if (!currentImages.contains(editingImage.value)) {
            currentImages.add(_editingImage.value!!)
        }

        _estateImages.value = Resource.Success(currentImages)

        moveToNextEstateImage()
    }

    fun onCancelEditing() {
        moveToNextEstateImage()
    }

    fun onDeletePhoto() {
        val currentImages = estateImages.value?.data?.toMutableList() ?: arrayListOf()
        currentImages.remove(_editingImage.value!!)

        _estateImages.value = Resource.Success(currentImages)

        moveToNextEstateImage()
    }

    fun onEstateImageClick(estateImage: EstateImage) {
        _iterator = Collections.singleton(estateImage).iterator()
        moveToNextEstateImage()
    }

    private fun moveToNextEstateImage() {
        _iterator?.let {
            if (it.hasNext()) {
                _editingImage.value = it.next()
            } else {
                _editingImage.value = null
            }
        }
    }

    fun onConfirmClick(
        address: String,
        description: String,
        type: String,
        surfaceArea: Float,
        roomCount: Int,
        bathroomCount: Int,
        bedroomCount: Int,
        price: Long,
        sold: Boolean,
    ) {
        _uploadState.value = Resource.Loading()
        Estate(
            estateId,
            _addressPlace?.address ?: address,
            _addressPlace?.let {
                LocalityUtils.getLocalityFromMapsAddressComponents(it.addressComponents!!.asList())
            } ?: estate.value?.data?.locality ?: "",
            estate.value?.data?.creationDateTs ?: Timestamp.now(),
            description,
            estate.value?.data?.previewImagePath,
            price,
            roomCount,
            bathroomCount,
            bedroomCount,
            estate.value?.data?.saleDateTs ?: if (sold) Timestamp.now() else null,
            surfaceArea,
            type,
            Firebase.auth.currentUser?.uid,
            null
        ).also {
            viewModelScope.launch(Dispatchers.IO) {

                val resource = estateRepository.uploadEstateImages(
                    it,
                    estateImages.value?.data ?: Collections.emptyList()
                )

                if (resource.errorType == ErrorType.NoInternet) {
                    launchSyncWorker()
                }

                _uploadState.postValue(resource)
            }
        }
    }

    private fun launchSyncWorker() {
        val workManager = WorkManager.getInstance(context)

        val workRequest = OneTimeWorkRequest.Builder(SyncWorker::class.java)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        workManager.enqueueUniqueWork(
            SYNC_WORKER_TAG,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

}