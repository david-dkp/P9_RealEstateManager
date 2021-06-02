package com.openclassrooms.realestatemanager.data.remote.maps

import android.util.LruCache
import com.openclassrooms.realestatemanager.data.models.dto.GeocodingResponse
import com.openclassrooms.realestatemanager.others.LRU_CACHE_SIZE

class GeocodingCache : LruCache<String, GeocodingResponse>(LRU_CACHE_SIZE)