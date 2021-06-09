package com.openclassrooms.realestatemanager.ui.login

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.UserRepository
import com.openclassrooms.realestatemanager.others.ErrorType
import com.openclassrooms.realestatemanager.others.MINIMUM_PASSWORD_LENGTH
import com.openclassrooms.realestatemanager.others.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel(
    val context: Context,
    val userRepository: UserRepository
) : ViewModel() {

    private val _loginState = MutableLiveData<Resource<Void>>()
    val loginState: LiveData<Resource<Void>> = _loginState

    private val _emailError = MutableLiveData<String>()
    val emailError: LiveData<String> = _emailError

    private val _passwordError = MutableLiveData<String>()
    val passwordError: LiveData<String> = _passwordError

    fun login(email: String, password: String) {

        val cleanEmail = email.trim()
        val cleanPassword = password.trim()

        _loginState.value = Resource.Loading()

        var hasError = false
        val emailValid = Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()
        val passwordValid = cleanPassword.length >= 6

        if (!emailValid) {
            hasError = true
            _emailError.value = context.getString(R.string.email_error)
        }

        if (!passwordValid) {
            hasError = true
            _passwordError.value = context.getString(R.string.password_min_length_error)
        }

        if (hasError) {
            _loginState.value = Resource.Error()
            return
        }

        viewModelScope.launch(Dispatchers.IO) {

            runCatching {
                val authResult = Firebase.auth.signInWithEmailAndPassword(cleanEmail, cleanPassword).await()
                _loginState.postValue(authResult.user?.let { Resource.Success() }
                    ?: Resource.Error())
            }.getOrElse {
                _loginState.postValue(Resource.Error(errorType = ErrorType.WrongCredential))
            }
        }
    }


}