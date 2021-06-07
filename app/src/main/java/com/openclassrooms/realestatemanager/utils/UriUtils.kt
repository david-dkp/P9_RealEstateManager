package com.openclassrooms.realestatemanager.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

object UriUtils {

    fun getUriName(context: Context, uri: Uri): String {
        val resolver = context.contentResolver
        val cursor = resolver.query(uri, null, null, null, null)

        return cursor!!.let {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            val name = cursor.getString(nameIndex)
            cursor.close()
            name
        }

    }

    fun getUrisName(context: Context, uris: List<Uri>): List<String> {
        return uris.map { getUriName(context, it) }
    }

}