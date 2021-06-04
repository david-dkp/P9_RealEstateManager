package com.openclassrooms.realestatemanager.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.data.UserRepository
import com.openclassrooms.realestatemanager.others.ErrorType
import com.openclassrooms.realestatemanager.others.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class LoginViewModel(
    val userRepository: UserRepository
) : ViewModel() {

    private val _loginState = MutableLiveData<Resource<Void>>()

    val loginState: LiveData<Resource<Void>> = _loginState

    fun login(email: String, password: String) {
        _loginState.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {

            runCatching {
                val authResult = Firebase.auth.signInWithEmailAndPassword(email, password).await()
                _loginState.postValue(authResult.user?.let { Resource.Success() }
                    ?: Resource.Error())
            }.getOrElse {
                _loginState.postValue(Resource.Error(errorType = ErrorType.WrongCredential))
            }
        }
    }

}