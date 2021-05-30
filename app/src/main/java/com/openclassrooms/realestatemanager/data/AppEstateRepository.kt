package com.openclassrooms.realestatemanager.data

import android.content.Context
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.data.local.daos.EstateDao
import com.openclassrooms.realestatemanager.data.local.daos.EstateImageDao
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.models.EstateImage
import com.openclassrooms.realestatemanager.data.remote.firebase.FirebaseHelper
import com.openclassrooms.realestatemanager.others.ErrorType
import com.openclassrooms.realestatemanager.others.Resource
import com.openclassrooms.realestatemanager.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.shareIn

class AppEstateRepository(
    private val context: Context,
    private val externalScope: CoroutineScope,
    private val firebaseHelper: FirebaseHelper,
    private val estateDao: EstateDao,
    private val estateImageDao: EstateImageDao
) : EstateRepository {

    override fun getEstatesFlow(): Flow<List<Estate>> {
        return estateDao.getEstatesFlow().shareIn(
            externalScope,
            SharingStarted.Eagerly,
            1
        )
    }

    override suspend fun getUserEstates(): Resource<List<Estate>> {

        if (!Utils.isInternetAvailable(context)) {
            return Resource.Error(
                getEstatesFlow().first().filter { it.userId == Firebase.auth.currentUser?.uid },
                ErrorType.NoInternet
            )
        }

        return try {
            val estates = firebaseHelper.getUserEstates()
            estateDao.deleteAllEstatesByUserId(Firebase.auth.currentUser!!.uid)
            estateDao.insertAllEstates(estates)
            Resource.Success(
                getEstatesFlow().first().filter { it.userId == Firebase.auth.currentUser?.uid })
        } catch (e: Exception) {
            Resource.Error(
                getEstatesFlow().first().filter { it.userId == Firebase.auth.currentUser?.uid },
                ErrorType.Unknown(e.message)
            )
        }
    }

    override suspend fun getEstateById(id: String): Resource<Estate> {
        if (!Utils.isInternetAvailable(context)) {
            return Resource.Error(
                getEstatesFlow().first().first { it.id == id },
                ErrorType.NoInternet
            )
        }

        return try {
            val estate = firebaseHelper.getEstateById(id)
            estateDao.insertEstate(estate)
            Resource.Success(estateDao.getEstatesFlow().first().first { it.id == id })
        } catch (e: Exception) {
            Resource.Error(
                estateDao.getEstatesFlow().first().first { it.id == id },
                ErrorType.Unknown(e.message)
            )
        }
    }

    override suspend fun getEstateImagesByEstateId(estateId: String): Resource<List<EstateImage>> {
        if (!Utils.isInternetAvailable(context)) {
            return Resource.Error(
                estateImageDao.getEstateImagesByEstateId(estateId),
                ErrorType.NoInternet
            )
        }

        return try {
            val estateImages = firebaseHelper.getEstateImagesByEstateId(estateId)
            estateImageDao.deleteAllImagesByEstateId(estateId)
            estateImageDao.insertAllEstateImages(estateImages)
            Resource.Success(estateImageDao.getEstateImagesByEstateId(estateId))
        } catch (e: Exception) {
            Resource.Error(
                estateImageDao.getEstateImagesByEstateId(estateId),
                ErrorType.Unknown(e.message)
            )
        }
    }

    override suspend fun refreshEstates(): Resource<Void> {
        if (!Utils.isInternetAvailable(context)) {
            return Resource.Error(errorType = ErrorType.NoInternet)
        }

        return try {
            val estates = firebaseHelper.getEstates()
            estateDao.deleteAllEstates()
            estateDao.insertAllEstates(estates)
            Resource.Success()
        } catch (e: Exception) {
            Log.d("debug", e.message ?: "")
            Resource.Error(errorType = ErrorType.Unknown(e.message))
        }
    }

    override suspend fun uploadEstateImages(
        estate: Estate,
        images: List<EstateImage>
    ): Resource<Void> {

        if (!Utils.isInternetAvailable(context)) {
            estate.isPushNeeded = true
            estateDao.insertEstate(estate)
            estateImageDao.deleteAllImagesByEstateId(estate.id)
            estateImageDao.insertAllEstateImages(images)

            return Resource.Error(errorType = ErrorType.NoInternet)
        }

        return try {
            firebaseHelper.uploadEstateImages(estate, images)
            estateDao.insertEstate(estate)
            estateImageDao.deleteAllImagesByEstateId(estate.id)
            estateImageDao.insertAllEstateImages(images)

            Resource.Success()
        } catch (e: Exception) {
            Resource.Error(errorType = ErrorType.Unknown(e.message))
        }
    }

}