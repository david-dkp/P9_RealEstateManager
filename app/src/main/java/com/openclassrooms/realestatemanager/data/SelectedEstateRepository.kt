package com.openclassrooms.realestatemanager.data

import androidx.lifecycle.LiveData

interface SelectedEstateRepository {
    fun getSelectedEstateId(): LiveData<String>
    fun setSelectedEstateId(id: String)
}