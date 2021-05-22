package com.openclassrooms.realestatemanager.others

sealed class ErrorType(message: String? = null) {
    object NoInternet : ErrorType()
    class Unknown(message: String? = null) : ErrorType(message)
}