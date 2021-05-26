package com.openclassrooms.realestatemanager.ui.login

import android.app.Activity.RESULT_OK
import androidx.activity.result.ActivityResult
import androidx.lifecycle.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.data.UserRepository
import com.openclassrooms.realestatemanager.others.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel(
    val userRepository: UserRepository
) : ViewModel() {

    private val _loginState = MutableLiveData<Resource<Void>>()

    val loginState: LiveData<Resource<Void>> = _loginState

    fun login(email: String, password: String) {
        _loginState.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            val authResult = Firebase.auth.signInWithEmailAndPassword(email, password).await()
            _loginState.postValue(authResult.user?.let { Resource.Success() } ?: Resource.Error())
        }

    }

    fun onStartGoogleIntent() {
        _loginState.value = Resource.Loading()
    }

    fun onGoogleActivityResult(activityResult: ActivityResult) {
        if (activityResult.resultCode == RESULT_OK) {
            viewModelScope.launch {
                val account = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data).await()
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                signInToFirebase(credential)
            }
        }
    }

    suspend fun signInToFirebase(credential: AuthCredential) {
        val authResult = Firebase.auth.signInWithCredential(credential).await()
        _loginState.postValue(authResult.user?.let { Resource.Success() } ?: Resource.Error())
    }

}