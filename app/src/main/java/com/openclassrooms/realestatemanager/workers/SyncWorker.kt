package com.openclassrooms.realestatemanager.workers

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.EstateRepository
import com.openclassrooms.realestatemanager.data.MapsRepository
import com.openclassrooms.realestatemanager.data.local.daos.EstateDao
import com.openclassrooms.realestatemanager.data.local.daos.EstateImageDao
import com.openclassrooms.realestatemanager.others.SYNC_NOTIFICATION_CHANNEL_ID
import com.openclassrooms.realestatemanager.ui.estatelist.EstateListActivity
import com.openclassrooms.realestatemanager.utils.LocalityUtils

class SyncWorker(
    val context: Context,
    val estateDao: EstateDao,
    val estateImageDao: EstateImageDao,
    val estateRepository: EstateRepository,
    val mapsRepository: MapsRepository,
    val workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val userUid = Firebase.auth.currentUser?.uid

        userUid?.let {
            val unsyncEstates = estateDao.getUnsyncEstatesById(userUid)

            if (unsyncEstates.isEmpty()) return Result.success()

            for (unsyncEstate in unsyncEstates) {

                unsyncEstate.address?.let { address ->
                    val result = mapsRepository.getGeocodingResult(address)
                    unsyncEstate.address = result.data?.formattedAddress
                    unsyncEstate.locality = result.data?.addressComponents?.let { components ->
                        LocalityUtils.getLocalityFromAddressComponents(
                            components
                        )
                    }
                }

                val images = estateImageDao.getEstateImagesByEstateId(unsyncEstate.id)
                estateRepository.uploadEstateImages(unsyncEstate, images)

                unsyncEstate.isPushNeeded = false
                estateDao.insertEstate(unsyncEstate)
            }

            notifyUser()

            Result.success()
        } ?: return Result.failure()

        return Result.failure()
    }

    private fun notifyUser() {

        val listActivityIntent = Intent(context, EstateListActivity::class.java)

        val notification = NotificationCompat.Builder(context, SYNC_NOTIFICATION_CHANNEL_ID)
            .setContentText(context.getString(R.string.estates_sync_notification_message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(PendingIntent.getActivity(context, 1, listActivityIntent, 0))
            .build()

        NotificationManagerCompat.from(context).notify(1, notification)
    }

}