package com.openclassrooms.realestatemanager.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import com.google.gson.annotations.SerializedName
import com.openclassrooms.realestatemanager.utils.IdUtils

@Entity(tableName = "estate_table")
data class Estate constructor (
    @DocumentId
    @PrimaryKey(autoGenerate = false)
    var id: String = IdUtils.generateId(20),

    var address: String? = null,

    @ColumnInfo(name = "locality")
    @get:PropertyName("locality") @set:PropertyName("locality")
    var locality: String? = null,

    @ColumnInfo(name = "creation_date_ts")
    @get:PropertyName("creation_date_ts") @set:PropertyName("creation_date_ts")
    var creationDateTs: Timestamp = Timestamp.now(),

    var description: String? = null,

    @ColumnInfo(name = "preview_image_path")
    @get:PropertyName("preview_image_path") @set:PropertyName("preview_image_path")
    var previewImagePath: String? = null,

    var price: Long? = null,

    @ColumnInfo(name = "room_count")
    @get:PropertyName("room_count") @set:PropertyName("room_count")
    var roomCount: Int? = null,

    @ColumnInfo(name = "bathroom_count")
    @get:PropertyName("bathroom_count") @set:PropertyName("bathroom_count")
    var bathroomCount: Int? = null,

    @ColumnInfo(name = "bedroom_count")
    @get:PropertyName("bedroom_count") @set:PropertyName("bedroom_count")
    var bedroomCount: Int? = null,

    @ColumnInfo(name = "sale_date_ts")
    @get:PropertyName("sale_date_ts") @set:PropertyName("sale_date_ts")
    var saleDateTs: Timestamp? = null,

    @ColumnInfo(name = "surface_area")
    @get:PropertyName("surface_area") @set:PropertyName("surface_area")
    var surfaceArea: Float? = null,

    var type: String? = null,

    @ColumnInfo(name = "user_id")
    @get:PropertyName("user_id") @set:PropertyName("user_id")
    var userId: String? = null,

    @ColumnInfo(name = "need_push")
    @Exclude
    var isPushNeeded: Boolean? = false
) {

    companion object {
        val DUMMY_ESTATES = listOf<Estate>(
            Estate(
                "1",
                "846 rue de LaRoche",
                "Ferney",
                Timestamp.now(),
                "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum",
                "estates_images/TrgbPJaxLgMEWhxcxszE/sample_estate.jpg",
                3000000,
                5, 4, 7,
                null,
                35.4f,
                "Penthouse",
                "none"
            ),
            Estate(
                "2",
                "846 rue de LaRoche",
                "Lyon",
                Timestamp.now(),
                "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum",
                "estates_images/TrgbPJaxLgMEWhxcxszE/sample_estate.jpg",
                3000000,
                5, 4, 7,
                null,
                35.4f,
                "Duplex",
                "none"
            ),
            Estate(
                "3",
                "846 rue de LaRoche",
                "Paris",
                Timestamp.now(),
                "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum",
                "estates_images/TrgbPJaxLgMEWhxcxszE/sample_estate.jpg",
                3000000,
                5, 4, 7,
                null,
                35.4f,
                "Duplex",
                "none"
            ),
            Estate(
                "4",
                "846 rue de LaRoche",
                "Ornex",
                Timestamp.now(),
                "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum",
                "estates_images/TrgbPJaxLgMEWhxcxszE/sample_estate.jpg",
                3000000,
                5, 4, 7,
                null,
                35.4f,
                "Duplex",
                "none"
            ),
            Estate(
                "5",
                "846 rue de LaRoche",
                "Marbre",
                Timestamp.now(),
                "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum",
                "estates_images/TrgbPJaxLgMEWhxcxszE/sample_estate.jpg",
                3000000,
                5, 4, 7,
                null,
                35.4f,
                "Flat",
                "none"
            ),
            Estate(
                "6",
                "245 Impasse de l'impasse",
                "New York",
                Timestamp.now(),
                "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum",
                "estates_images/TrgbPJaxLgMEWhxcxszE/sample_estate.jpg",
                20050000,
                5, 2, 1,
                null,
                35.4f,
                "House",
                "none"
            ),
        )
    }

}
