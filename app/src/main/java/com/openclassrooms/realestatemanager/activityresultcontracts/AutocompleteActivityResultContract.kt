package com.openclassrooms.realestatemanager.activityresultcontracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

class AutocompleteActivityResultContract : ActivityResultContract<Intent, Place?>() {
    override fun createIntent(context: Context, input: Intent?): Intent {
        return input ?: Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN,
            Place.Field.values().toList()
        ).build(context)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Place? {
        return if (resultCode == Activity.RESULT_OK) {
            Autocomplete.getPlaceFromIntent(intent!!)
        } else {
            null
        }
    }
}