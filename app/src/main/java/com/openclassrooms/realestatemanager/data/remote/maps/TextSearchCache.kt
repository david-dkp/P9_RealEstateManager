package com.openclassrooms.realestatemanager.data.remote.maps

import android.util.LruCache
import com.openclassrooms.realestatemanager.data.models.dto.TextSearchResponse
import com.openclassrooms.realestatemanager.others.LRU_CACHE_SIZE

class TextSearchCache : LruCache<String, TextSearchResponse>(LRU_CACHE_SIZE)