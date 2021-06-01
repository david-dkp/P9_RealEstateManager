package com.openclassrooms.realestatemanager.ui.estatelist

import android.content.Context
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.data.EstateRepository
import com.openclassrooms.realestatemanager.data.UserRepository
import com.openclassrooms.realestatemanager.others.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class EstateListViewModel(
    val context: Context,
    val estateRepository: EstateRepository,
    val userRepository: UserRepository
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

    private val _refreshState = MutableLiveData<Resource<Void>>()
    val refreshState: LiveData<Resource<Void>> = _refreshState

    init {
        viewModelScope.launch {
            estateRepository.refreshEstates()
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun refreshEstates() {
        _refreshState.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {

            _refreshState.postValue(estateRepository.refreshEstates())

        }
    }
}