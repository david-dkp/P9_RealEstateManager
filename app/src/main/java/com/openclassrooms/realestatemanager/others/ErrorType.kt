package com.openclassrooms.realestatemanager.others

sealed class ErrorType(val message: String? = null) {
    object NoInternet : ErrorType()
    object CantFoundAddress : ErrorType()
    object WrongCredential : ErrorType()

    object LocationDisabled : ErrorType()
    object NoLocationPermission : ErrorType()

    class Unknown(message: String?) : ErrorType(message)
}