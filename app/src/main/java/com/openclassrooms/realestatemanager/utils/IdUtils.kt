package com.openclassrooms.realestatemanager.utils

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.*

object IdUtils {

    fun generateId(): String {
        return UUID.randomUUID().toString()
    }

    fun generateId(maxLength: Int): String {
        generateId().also {
            if (it.length <= maxLength) {
                return it
            } else {
                return it.substring(0, maxLength)
            }
        }
    }

}