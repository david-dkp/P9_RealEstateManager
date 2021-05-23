package com.openclassrooms.realestatemanager.data

import android.content.Context
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.data.local.daos.UserDao
import com.openclassrooms.realestatemanager.data.models.User
import com.openclassrooms.realestatemanager.data.remote.user.FirebaseHelper
import com.openclassrooms.realestatemanager.others.ErrorType
import com.openclassrooms.realestatemanager.others.Resource
import com.openclassrooms.realestatemanager.utils.Utils
import java.lang.Exception

class AppUserRepository(
    private val context: Context,
    private val userDao: UserDao,
    private val firebaseHelper: FirebaseHelper
) : UserRepository {

    override suspend fun getUsers(): Resource<List<User>> {

        if (!Utils.isInternetAvailable(context)) {
            return Resource.Error(userDao.getUsers(), ErrorType.NoInternet)
        }

        return try {
            userDao.insertAllUsers(firebaseHelper.getUsers())
            Resource.Success(userDao.getUsers())
        } catch (e: Exception) {
            Resource.Error(userDao.getUsers(), ErrorType.Unknown(e.message))
        }
    }

    override suspend fun addUser(user: User): Resource<Void> {

        if (!Utils.isInternetAvailable(context)) {
            val userId = Firebase
                .firestore
                .collection("users")
                .document()
                .id

            user.id = userId
            user.isPushNeeded = true

            userDao.insertUser(user)
            return Resource.Error(null, ErrorType.NoInternet)
        }

        return try {
            val updatedUser = firebaseHelper.addUser(user)
            userDao.insertUser(updatedUser)
            Resource.Success()
        } catch (e: Exception) {
            Resource.Error(errorType = ErrorType.Unknown(e.message))
        }
    }
}