package com.openclassrooms.realestatemanager.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class AppSelectedEstateRepository : SelectedEstateRepository {

    private val _selectedEstateId = MutableLiveData<String>()

    override fun getSelectedEstateId(): LiveData<String> {
        return _selectedEstateId
    }

    override fun setSelectedEstateId(id: String) {
        _selectedEstateId.value = id
    }

}