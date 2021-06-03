package com.openclassrooms.realestatemanager.data.remote.maps

import android.util.LruCache
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.data.models.responses.NearbySearchResponse
import com.openclassrooms.realestatemanager.others.LRU_CACHE_SIZE

class NearbySearchCache : LruCache<LatLng, NearbySearchResponse>(LRU_CACHE_SIZE)