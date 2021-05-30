package com.openclassrooms.realestatemanager.ui.estatelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.data.EstateRepository
import com.openclassrooms.realestatemanager.data.UserRepository
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.models.User
import com.openclassrooms.realestatemanager.others.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class EstateListViewModel(
    estateRepository: EstateRepository,
    userRepository: UserRepository
) : ViewModel() {

    private val auth = Firebase.auth

    @ExperimentalCoroutinesApi
    val isLoggedIn = callbackFlow {

        val authListener: ((FirebaseAuth?) -> Unit) = {
            trySend(it != null && it.currentUser != null)
        }

        auth.addAuthStateListener(authListener)

        awaitClose { auth.removeAuthStateListener(authListener) }
    }

    val user = liveData {
        emit(
            userRepository.getCurrentUser()
        )
    }

    val estates = estateRepository.getEstatesFlow()

    init {
        viewModelScope.launch{
            estateRepository.refreshEstates()
        }
    }

    fun logout() {
        auth.signOut()
    }
}