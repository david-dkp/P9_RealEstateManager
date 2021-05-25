package com.openclassrooms.realestatemanager.ui.estate

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.models.EstateImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EstateViewModel : ViewModel() {

    private val _location = MutableStateFlow(
        LatLng(0.0, 0.0)
    )

    val location: StateFlow<LatLng> = _location

    private val _estate = MutableStateFlow(
        Estate(
            "random",
            "335 rue des pralets",
            "Ornex",
            Timestamp.now(),
            "This is a description",
            "estates_im",
            300000000,
            8, 5, 2,
            Timestamp.now(),
            35.4f,
            "Appart",
            "no"
        )
    )

    val estate: StateFlow<Estate> = _estate

}