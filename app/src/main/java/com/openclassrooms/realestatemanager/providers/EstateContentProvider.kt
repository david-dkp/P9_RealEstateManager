package com.openclassrooms.realestatemanager.providers

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import androidx.room.Room
import com.openclassrooms.realestatemanager.contracts.AppDatabaseContract.AUTHORITY
import com.openclassrooms.realestatemanager.contracts.AppDatabaseContract.Estate
import com.openclassrooms.realestatemanager.contracts.AppDatabaseContract.EstateImage
import com.openclassrooms.realestatemanager.contracts.AppDatabaseContract.User
import com.openclassrooms.realestatemanager.data.local.AppDatabase
import com.openclassrooms.realestatemanager.data.local.daos.EstateDao
import com.openclassrooms.realestatemanager.data.local.daos.EstateImageDao
import com.openclassrooms.realestatemanager.data.local.daos.UserDao
import com.openclassrooms.realestatemanager.others.APP_DATABASE_NAME
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent
import java.lang.IllegalArgumentException

const val MATCH_ESTATE_TABLE = 1
const val MATCH_ESTATE_ITEM = 2
const val MATCH_ESTATE_IMAGE_TABLE = 3
const val MATCH_ESTATE_IMAGE_ITEM = 4
const val MATCH_USER_TABLE = 5
const val MATCH_USER_ITEM = 6

private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
    addURI(AUTHORITY, Estate.TABLE_NAME, MATCH_ESTATE_TABLE)
    addURI(AUTHORITY, "${Estate.TABLE_NAME}/*", MATCH_ESTATE_ITEM)

    addURI(AUTHORITY, EstateImage.TABLE_NAME, MATCH_ESTATE_IMAGE_TABLE)
    addURI(AUTHORITY, "${EstateImage.TABLE_NAME}/*", MATCH_ESTATE_IMAGE_ITEM)

    addURI(AUTHORITY, User.TABLE_NAME, MATCH_USER_TABLE)
    addURI(AUTHORITY, "${User.TABLE_NAME}/*", MATCH_USER_ITEM)
}

class EstateContentProvider : ContentProvider(), KoinComponent {

    private  lateinit var appDatabase: AppDatabase
    private var estateDao: EstateDao? = null
    private var estateImageDao: EstateImageDao? = null
    private var userDao: UserDao? = null

    override fun onCreate(): Boolean {
        appDatabase = Room.databaseBuilder(context!!, AppDatabase::class.java, APP_DATABASE_NAME)
            .build()

        appDatabase.apply {
            estateDao = estateDao()
            estateImageDao = estateImageDao()
            userDao = userDao()
        }

        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {

        when (uriMatcher.match(uri)) {
            MATCH_ESTATE_TABLE -> {
                return estateDao?.getEstatesCursor()
            }

            MATCH_ESTATE_ITEM -> {
                val id = uri.lastPathSegment!!
                return estateDao?.getEstateByIdCursor(id)
            }

            MATCH_ESTATE_IMAGE_TABLE -> {
                return estateImageDao?.getEstateImagesCursor()
            }

            MATCH_ESTATE_IMAGE_ITEM -> {
                val id = uri.lastPathSegment!!
                return estateImageDao?.getEstateImageByIdCursor(id)
            }

            MATCH_USER_TABLE -> {
                return userDao?.getUsersCursor()
            }

            MATCH_USER_ITEM -> {
                val id = uri.lastPathSegment!!
                return userDao?.getUserByIdCursor(id)
            }

            else -> throw IllegalArgumentException()
        }

    }

    override fun getType(uri: Uri): String {
        val hasId = (uriMatcher.match(uri) == 2) ||(uriMatcher.match(uri) == 4) ||(uriMatcher.match(uri) == 6)

        val tableName = uri.pathSegments[uri.pathSegments.size - if (hasId) 2 else 1]

        return "vnd.android.cursor.${if (hasId) "item" else "dir"}/vng.$AUTHORITY.$tableName"
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {

        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }

}