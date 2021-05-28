package com.openclassrooms.realestatemanager.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.gson.annotations.SerializedName

@Entity(tableName = "estate_table")
data class Estate(
    @DocumentId
    @PrimaryKey(autoGenerate = false)
    var id: String,

    var address: String,

    @ColumnInfo(name = "locality")
    @SerializedName("locality")
    var locality: String = "",

    @ColumnInfo(name = "creation_date_ts")
    @SerializedName("creation_date_ts")
    var creationDateTs: Timestamp,

    var description: String,

    @ColumnInfo(name = "preview_image_path")
    @SerializedName("preview_image_path")
    var previewImagePath: String,

    var price: Long,

    @ColumnInfo(name = "room_count")
    @SerializedName("room_count")
    var roomCount: Int,

    @ColumnInfo(name = "bathroom_count")
    @SerializedName("bathroom_count")
    var bathroomCount: Int,

    @ColumnInfo(name = "bedroom_count")
    @SerializedName("bedroom_count")
    var bedroomCount: Int,

    @ColumnInfo(name = "sale_date_ts")
    @SerializedName("sale_date_ts")
    var saleDateTs: Timestamp?,

    @ColumnInfo(name = "surface_area")
    @SerializedName("surface_area")
    var surfaceArea: Float,

    var type: String,

    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    var userId: String,

    @ColumnInfo(name = "need_push")
    @Exclude
    var isPushNeeded: Boolean = false
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
