package com.openclassrooms.realestatemanager.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.data.EstateRepository
import com.openclassrooms.realestatemanager.data.local.daos.EstateDao
import com.openclassrooms.realestatemanager.data.local.daos.EstateImageDao
import java.lang.Exception

class SyncWorker(
    val context: Context,
    val estateDao: EstateDao,
    val estateImageDao: EstateImageDao,
    val estateRepository: EstateRepository,
    val workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
//        val userUid = Firebase.auth.uid
//
//        userUid?.let {
//            val unsyncEstates = estateDao.getUnsyncEstatesById(userUid)
//
//            for (unsyncEstate in unsyncEstates) {
//                val images = estateImageDao.getEstateImagesByEstateId(unsyncEstate.id)
//                estateRepository.updateEstateImages(unsyncEstate, images)
//            }
//
//
//        } ?: throw Exception("User not logged in, couldn't continue syncing")
//
        return Result.failure()
    }

}