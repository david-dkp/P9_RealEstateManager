package com.openclassrooms.realestatemanager.others

sealed class Resource<T>(data: T? = null, errorType: ErrorType? = null) {
    class Success<T>(data: T? = null) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(data: T? = null, errorType: ErrorType? = null) : Resource<T>(data, errorType)
}
