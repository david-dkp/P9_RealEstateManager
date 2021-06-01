package com.openclassrooms.realestatemanager.providers

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
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

private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
    addURI(AUTHORITY, Estate.TABLE_NAME, 1)
    addURI(AUTHORITY, "${Estate.TABLE_NAME}/#", 2)

    addURI(AUTHORITY, EstateImage.TABLE_NAME, 3)
    addURI(AUTHORITY, "${EstateImage.TABLE_NAME}/#", 4)

    addURI(AUTHORITY, User.TABLE_NAME, 5)
    addURI(AUTHORITY, "${User.TABLE_NAME}/#", 6)
}

class EstateContentProvider : ContentProvider() {

    private lateinit var appDatabase: AppDatabase

    private var estateDao: EstateDao? = null
    private var estateImageDao: EstateImageDao? = null
    private var userDao: UserDao? = null

    override fun onCreate(): Boolean {
        appDatabase = Room.databaseBuilder(
            context!!,
            AppDatabase::class.java,
            APP_DATABASE_NAME
        ).build().apply {
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
        return TODO()
    }

    override fun getType(uri: Uri): String? {
        val hasId = ContentUris.parseId(uri) != -1L

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