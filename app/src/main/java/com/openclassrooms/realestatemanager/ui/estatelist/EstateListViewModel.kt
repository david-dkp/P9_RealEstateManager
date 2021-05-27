package com.openclassrooms.realestatemanager.ui.estatelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.data.EstateRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class EstateListViewModel(
    estateRepository: EstateRepository
) : ViewModel(){

    private val auth = Firebase.auth

    @ExperimentalCoroutinesApi
    val isLoggedIn = callbackFlow {

        val authListener: ((FirebaseAuth?) -> Unit) = {
            trySend(it != null && it.currentUser != null)
        }

        auth.addAuthStateListener(authListener)

        awaitClose { auth.removeAuthStateListener(authListener) }
    }

    fun logout() {
        auth.signOut()
    }
}