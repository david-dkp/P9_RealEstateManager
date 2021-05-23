package com.openclassrooms.realestatemanager.data.remote.user

import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
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

    override suspend fun addEstate(estate: Estate): Estate {
        return estatesRef
            .add(estate)
            .await()
            .let {
            estate.id = it.id
            estate
        }
    }

    override suspend fun updateEstate(estate: Estate): Estate{

        estatesRef
            .document(estate.id)
            .set(estate)
            .await()

        return estate
    }

    override suspend fun updateEstateImages(estateId: String, images: List<EstateImage>): List<EstateImage> {

        val oldImagesRefs = estatesImagesRef
            .whereEqualTo("estate_id", estateId)
            .get()
            .await()
            .documents

        firestore.runBatch {
            //First delete all images
            for (oldImageRef in oldImagesRefs) {
                it.delete(oldImageRef.reference)
            }

            //Then add new ones
            for (image in images) {
                it.set(estatesImagesRef.document().apply { image.id = id }, image)
            }
        }.await()

        return images
    }

    override suspend fun getUsers(): List<User> {
        return usersRef
            .get()
            .await()
            .toObjects(User::class.java)
    }

    override suspend fun addUser(user: User): User {
        if (firebaseUser == null) throw FirebaseAuthInvalidUserException("", "")
        usersRef.document(firebaseUser.uid).set(user)

        return user
    }
}