package com.openclassrooms.realestatemanager.data.remote.firebase

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.openclassrooms.realestatemanager.data.models.domain.Estate
import com.openclassrooms.realestatemanager.data.models.domain.EstateImage
import com.openclassrooms.realestatemanager.data.models.domain.User
import com.openclassrooms.realestatemanager.utils.UriUtils
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AppFirebaseHelper(
    private val context: Context
) : FirebaseHelper {

    private val firebaseUser = Firebase.auth.currentUser
    private val firestore = Firebase.firestore
    private val storage = Firebase.storage
    private val auth = Firebase.auth

    private val estatesRef = firestore.collection("estates")
    private val estatesImagesRef = firestore.collection("estates_images")
    private val usersRef = firestore.collection("users")

    @ExperimentalCoroutinesApi
    override fun isLoggedInFlow() = callbackFlow {

        val authListener: ((FirebaseAuth?) -> Unit) = {
            trySend(it != null && it.currentUser != null)
        }

        auth.addAuthStateListener(authListener)

        awaitClose { auth.removeAuthStateListener(authListener) }
    }

    override fun logout() {
        auth.signOut()
    }

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

            val estateImagesPaths = estateImages.map {
                it.imagePath
            }

            val existingImagesRefs =
                storage
                    .reference
                    .child("/estates_images/${estate.id}")
                    .listAll()
                    .await()
                    .items

            val existingImagesPaths = existingImagesRefs.map { it.path }

            val differs: ArrayList<Deferred<Any>> = arrayListOf()

            //Delete images
            for (existingImageRef in existingImagesRefs) {
                if (!estateImagesPaths.contains(existingImageRef.path)) {
                    differs.add(
                        async {
                            existingImageRef.delete().await()
                        }
                    )
                }
            }

            //Upload images
            for (estateImage in estateImages) {
                if (existingImagesPaths.contains(estateImage.imagePath)) continue

                estateImage.uri?.let {
                    val uri = Uri.parse(it)
                    val uriName = UriUtils.getUriName(context.contentResolver, uri)
                    val path = "/estates_images/${estate.id}/$uriName"
                    estateImage.imagePath = path

                    differs.add(
                        async {
                            storage.getReference(
                                path
                            ).putFile(uri).await()

                            UriUtils.deleteFile(context.contentResolver, uri)
                        }
                    )
                }
            }

            differs.awaitAll()

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

            estate.photoCount = estateImages.size
            estate.previewImagePath = estateImages.firstOrNull()?.imagePath

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