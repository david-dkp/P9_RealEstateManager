package com.openclassrooms.realestatemanager.contracts

import android.net.Uri

object AppDatabaseContract {

    const val AUTHORITY = "com.openclassrooms.realestatemanager.provider"

    object Estate {
        const val TABLE_NAME = "estate_table"
        val CONTENT_URI = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
        const val ID = "id"
        const val ADDRESS = "address"
        const val LOCALITY = "locality"
        const val CREATION_DATE_TS = "creation_date_ts"
        const val DESCRIPTION = "description"
        const val PREVIEW_IMAGE_PATH = "preview_image_path"
        const val PHOTO_COUNT = "photo_count"
        const val PRICE = "price"
        const val ROOM_COUNT = "room_count"
        const val BATHROOM_COUNT = "bathroom_count"
        const val BEDROOM_COUNT = "bedroom_count"
        const val SALE_DATE_TS = "sale_date_ts"
        const val SURFACE_AREA = "surface_area"
        const val TYPE = "type"
        const val USER_ID = "user_id"
        const val NEED_PUSH = "need_push"
    }

    object EstateImage {
        const val TABLE_NAME = "estate_image_table"
        val CONTENT_URI = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
        const val ID = "id"
        const val DESCRIPTION = "description"
        const val ESTATE_ID = "estate_id"
        const val IMAGE_PATH = "image_path"
        const val URI = "uri"
    }

    object User {
        const val TABLE_NAME = "user_table"
        val CONTENT_URI = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
        const val ID = "id"
        const val EMAIL = "email"
        const val FIRST_NAME = "first_name"
        const val LAST_NAME = "last_name"
        const val PHONE_NUMBER = "phone_number"
        const val IMAGE_PATH = "image_path"
        const val NEED_PUSH = "need_push"
    }

}