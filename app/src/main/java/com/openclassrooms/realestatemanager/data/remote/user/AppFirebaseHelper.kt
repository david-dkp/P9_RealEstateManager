package com.openclassrooms.realestatemanager.data.remote.user

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.models.EstateImage
import com.openclassrooms.realestatemanager.data.models.User
import kotlinx.coroutines.tasks.await

class AppFirebaseHelper : FirebaseHelper {

    private val firebaseUser = Firebase.auth.currentUser
    private val firestore = Firebase.firestore

    private val estatesRef = firestore.collection("estates")
    private val estatesImagesRef = firestore.collection("estates_images")
    private val usersRef = firestore.collection("estates_images")

    override suspend fun getUserEstates(): List<Estate> {
        return estatesRef
            .whereEqualTo("user_id", firebaseUser?.uid)
            .get()
            .await()
            .toObjects(Estate::class.java)
    }

    override suspend fun getEstates(): List<Estate> {
        return estatesRef
            .get()
            .await()
            .toObjects(Estate::class.java)
    }

    override suspend fun getEstateImagesByEstateId(estateId: String): List<EstateImage> {
        return estatesImagesRef
            .whereEqualTo("estate_id", estateId)
            .get()
            .await()
            .toObjects(EstateImage::class.java)
    }

    override suspend fun addEstate(estate: Estate) {
        estatesRef.add(estate).await()
    }

    override suspend fun updateEstate(estate: Estate) {
        estatesRef.document(estate.id!!).set(estate)
    }

    override suspend fun updateEstateImages(estateId: String, images: List<EstateImage>) {
        estatesImagesRef
            .whereEqualTo("estate_id", estateId)
            .get()
            .await()
            .documents.forEach {
                estatesImagesRef.document(it.id).delete()
            }

        for (image in images) {
            estatesImagesRef.add(image).await()
        }
    }

    override suspend fun getUsers(): List<User> {
        return usersRef
            .get()
            .await()
            .toObjects(User::class.java)
    }

    override suspend fun addUser(user: User) {
        if (firebaseUser == null) throw FirebaseAuthInvalidUserException("", "")
        usersRef.document(firebaseUser.uid).set(user)
    }
}