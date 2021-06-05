package com.openclassrooms.realestatemanager.others

import com.google.android.libraries.places.api.model.Place

//Maps
const val MAPS_API_BASE_URL = "https://maps.googleapis.com/maps/api/"
const val STATIC_MAP_ZOOM_LEVEL = 15f
const val NEARBY_SEARCH_RADIUS = 300

//Conversion
const val DOLLAR_TO_EURO_COEFFICIENT = 0.82

//
const val MINIMUM_PASSWORD_LENGTH = 6

//Database
const val APP_DATABASE_NAME = "realestatemanager.db"
const val APP_DATABASE_VERSION = 1

//Extras
const val EXTRA_ESTATE_ID = "EXTRA_ESTATE_ID"

//Arguments
const val KEY_FILTER_DATA = "KEY_FILTER_DATA"

//Preferences keys
const val KEY_LOCATION_LATITUDE = "KEY_LOCATION_LATITUDE"
const val KEY_LOCATION_LONGITUDE = "KEY_LOCATION_LONGITUDE"

//TAGS
const val ADD_PHOTO_DIALOG_FRAGMENT_TAG = "ADD_PHOTO_DIALOG_FRAGMENT_TAG"
const val FILTER_DIALOG_FRAGMENT_TAG = "FILTER_DIALOG_FRAGMENT_TAG"

val ADDRESS_COMPONENTS_LOCALITY_TYPES = listOf(
    "administrative_area_level_1",
    "administrative_area_level_2",
    "administrative_area_level_3",
    "administrative_area_level_4",
    "administrative_area_level_5",
    "locality",
    "political"
)

//Notification
const val SYNC_NOTIFICATION_CHANNEL_ID = "SYNC_NOTIFICATION_CHANNEL"

//Workers
const val SYNC_WORKER_TAG = "SYNC_WORKER_TAG"

//Cache
const val LRU_CACHE_SIZE = 4 * 1024 * 1024

//Filter
val FILTER_TYPES = listOf(
    Place.Type.SCHOOL,
    Place.Type.STORE,
    Place.Type.RESTAURANT,
    Place.Type.TRAIN_STATION,
    Place.Type.AIRPORT
)

//Prefs keys
const val PREF_KEY_THEME = "theme"
const val PREF_KEY_LAST_LOCATION_LATITUDE = "last_location_latitude"
const val PREF_KEY_LAST_LOCATION_LONGITUDE = "last_location_longitude"

//Prefs values
const val PREF_VALUE_LIGHT_MODE = "light_mode"
const val PREF_VALUE_DARK_MODE = "dark_mode"
const val PREF_VALUE_SYSTEM_DEFAULT = "system_default"