package com.openclassrooms.realestatemanager.utils

import android.content.Context
import android.content.res.Configuration

object UiUtils {

    fun isDarkMode(context: Context): Boolean {
        val config = context.resources.configuration
        val nightModeMask = Configuration.UI_MODE_NIGHT_MASK
        val nightMode = config.uiMode and nightModeMask

        return nightMode == Configuration.UI_MODE_NIGHT_YES
    }

}