package com.openclassrooms.realestatemanager.activityresultcontracts

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract


class LocationSettingsContract : ActivityResultContract<Void, Void?>() {

    override fun createIntent(context: Context, input: Void?): Intent {
        return Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Void? {
        return null
    }

}