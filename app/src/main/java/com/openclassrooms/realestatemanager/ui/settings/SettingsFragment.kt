package com.openclassrooms.realestatemanager.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.others.PREF_KEY_THEME
import com.openclassrooms.realestatemanager.others.PREF_VALUE_DARK_MODE
import com.openclassrooms.realestatemanager.others.PREF_VALUE_LIGHT_MODE
import com.openclassrooms.realestatemanager.others.PREF_VALUE_SYSTEM_DEFAULT

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == PREF_KEY_THEME) {
            when (sharedPreferences?.getString(PREF_KEY_THEME, "system_default")) {
                PREF_VALUE_LIGHT_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                PREF_VALUE_DARK_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                PREF_VALUE_SYSTEM_DEFAULT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

}