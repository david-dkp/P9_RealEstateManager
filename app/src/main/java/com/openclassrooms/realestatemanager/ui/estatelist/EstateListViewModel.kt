package com.openclassrooms.realestatemanager.ui.estatelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.data.EstateRepository
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.models.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class EstateListViewModel(
    estateRepository: EstateRepository
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

    val user = liveData<User> {
        emit(
            User(
                "no",
                "david.dekeuwer@gmail.com",
                "David",
                "Dekeuwer",
                "0781923016",
                "profile_images/CWMVuHGwipH0EOut7xaS.jpg",
                false
            )
        )
    }

    val estates = liveData<List<Estate>> {
        emit(Estate.DUMMY_ESTATES)
    }

    fun logout() {
        auth.signOut()
    }
}