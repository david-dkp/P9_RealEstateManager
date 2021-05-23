package com.openclassrooms.realestatemanager.others

sealed class ErrorType(message: String? = null) {
    object NoInternet : ErrorType()
    object CantFoundAddress: ErrorType()
    class Unknown(message: String? = null) : ErrorType(message)
}