package com.openclassrooms.realestatemanager.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

object UriUtils {

    fun getUriName(contentResolver: ContentResolver, uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)

        return cursor!!.let {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            val name = cursor.getString(nameIndex)
            cursor.close()
            name
        }

    }

    fun deleteFile(contentResolver: ContentResolver, uri: Uri): Int {
        return contentResolver.delete(uri, null, null)
    }

    fun deleteFile(contentResolver: ContentResolver, uri: List<Uri>): List<Int> {
        return uri.map { deleteFile(contentResolver, it) }
    }

}