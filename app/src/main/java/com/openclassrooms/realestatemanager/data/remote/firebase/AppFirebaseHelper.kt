package com.openclassrooms.realestatemanager.data.remote.firebase

import android.content.Context
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.openclassrooms.realestatemanager.data.models.domain.Estate
import com.openclassrooms.realestatemanager.data.models.domain.EstateImage
import com.openclassrooms.realestatemanager.data.models.domain.User
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
    private val usersRef = firestore.collection("users")

    override suspend fun getUserEstates(): List<Estate> {

        return estatesRef
            .whereEqualTo("user_id", firebaseUser?.uid)
            .get()
            .await()
            .toObjects(Estate::class.java)
    }

    override suspend fun getUserById(id: String): User {
        return usersRef
            .document(id)
            .get()
            .await()
            .toObject(User::class.java)!!
    }

    override suspend fun getEstates(): List<Estate> {
        return estatesRef
            .get()
            .await()
            .toObjects(Estate::class.java)
    }

    override suspend fun getEstateById(id: String): Estate {
        return estatesRef
            .whereEqualTo(FieldPath.documentId(), id)
            .get()
            .await()
            .toObjects(Estate::class.java)
            .first()
    }

    override suspend fun getEstateImagesByEstateId(estateId: String): List<EstateImage> {
        return estatesImagesRef
            .whereEqualTo("estate_id", estateId)
            .get()
            .await()
            .toObjects(EstateImage::class.java)
    }

    override suspend fun uploadEstateImages(estate: Estate, estateImages: List<EstateImage>) =
        coroutineScope {

            val existingImagesRef =
                storage
                    .reference
                    .child("estates_images/${estate.id}")
                    .listAll()
                    .await()
                    .items

            val uploadPaths = estateImages.mapNotNull {
                it.imagePath
            }

            val differs: ArrayList<Deferred<Any>> = arrayListOf()

            for (existingImageRef in existingImagesRef) {
                if (!uploadPaths.contains(existingImageRef.path)) {
                    differs.add( async { existingImageRef.delete().await() } )
                    existingImagesRef.remove(existingImageRef)
                } else {

                }
            }



            estatesImagesRef
                .whereEqualTo("estate_id", estate.id)
                .get()
                .await()
                .documents
                .map { async { it.reference.delete().await() } }
                .awaitAll()

            estateImages.map {
                async {
                    estatesImagesRef
                        .document(it.id)
                        .set(it)
                        .await()
                }
            }.awaitAll()

            estatesRef
                .document(estate.id)
                .set(estate)
                .await()

            Unit
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