package com.openclassrooms.realestatemanager.data

import android.content.Context
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.data.local.daos.UserDao
import com.openclassrooms.realestatemanager.data.models.domain.User
import com.openclassrooms.realestatemanager.data.remote.firebase.FirebaseHelper
import com.openclassrooms.realestatemanager.others.ErrorType
import com.openclassrooms.realestatemanager.others.Resource
import com.openclassrooms.realestatemanager.utils.IdUtils
import com.openclassrooms.realestatemanager.utils.Utils

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
            val users = firebaseHelper.getUsers()
            userDao.deleteAllUsers()
            userDao.insertAllUsers(users)
            Resource.Success(userDao.getUsers())
        } catch (e: Exception) {
            Resource.Error(userDao.getUsers(), ErrorType.Unknown(e.message))
        }
    }

    override suspend fun addUser(user: User): Resource<Void> {

        if (!Utils.isInternetAvailable(context)) {
            val userId = IdUtils.generateId(20)

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

    override suspend fun getCurrentUser(): Resource<User> {

        val userId = Firebase.auth.currentUser?.uid ?: return Resource.Error()

        if (!Utils.isInternetAvailable(context)) {
            return Resource.Error(userDao.getUserById(userId), ErrorType.NoInternet)
        }

        return try {
            val user = firebaseHelper.getUserById(userId)
            userDao.insertUser(user)
            Resource.Success(userDao.getUserById(userId))
        } catch (e: Exception) {
            Log.d("debug", e.message ?: "")
            Resource.Error(userDao.getUserById(userId), ErrorType.Unknown(e.message))
        }
    }


}