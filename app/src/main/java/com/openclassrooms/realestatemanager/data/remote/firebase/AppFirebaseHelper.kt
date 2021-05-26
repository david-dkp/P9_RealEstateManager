package com.openclassrooms.realestatemanager.data.remote.firebase

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.models.EstateImage
import com.openclassrooms.realestatemanager.data.models.User
import com.openclassrooms.realestatemanager.utils.UriUtils
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

class AppFirebaseHelper(
    private val context: Context
) : FirebaseHelper {

    private val firebaseUser = Firebase.auth.currentUser
    private val firestore = Firebase.firestore
    private val storage = Firebase.storage

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

    override suspend fun uploadEstateImages(estateId: String, estateImages: List<EstateImage>): List<EstateImage> = coroutineScope {

        val imagesUri = estateImages.map { Uri.parse(it.uri) }.toMutableList()
        val imagesName = UriUtils.getUrisName(context, imagesUri).toMutableList()

        val existingImagesRef = storage.reference.child("estates_images/$estateId").listAll().await().items

        val uploadDiffers: ArrayList<Deferred<Any>> =  arrayListOf()

        //Prevent for re-upload image if image is already in storage
        for (existingImageRef in existingImagesRef) {
            val imageIndex = imagesName.indexOf(existingImageRef.name)

            if (imageIndex != -1) {
                imagesUri.removeAt(imageIndex)
                imagesName.removeAt(imageIndex)
                continue
            } else {
                uploadDiffers.add(async { existingImageRef.delete().await() })
            }
        }

        for ((index, imageUri) in imagesUri.withIndex()) {
            uploadDiffers.add(
                async {
                    storage
                        .reference
                        .child("estates_images/$estateId/${imagesName[index]}")
                        .putFile(imageUri)
                        .await()
                }
            )
        }

        uploadDiffers.awaitAll()

        firestore
            .collection("estates_images")
            .whereEqualTo("estate_id", estateId)
            .get()
            .await()
            .documents
            .map { async { it.reference.delete().await() } }
            .awaitAll()

        estateImages.map {
            async {
                firestore
                    .collection("estates_images")
                    .add(it)
                    .await()
                    .also { docRef ->
                        it.id = docRef.id
                    }
            }
        }.awaitAll()

        estateImages
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