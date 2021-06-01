package com.openclassrooms.realestatemanager.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.openclassrooms.realestatemanager.contracts.AppDatabaseContract.Estate
import com.openclassrooms.realestatemanager.utils.IdUtils

@Entity(tableName = Estate.TABLE_NAME)
@IgnoreExtraProperties
data class Estate constructor(

    @ColumnInfo(name = Estate.ID)
    @DocumentId
    @PrimaryKey(autoGenerate = false)
    var id: String = IdUtils.generateId(20),

    @ColumnInfo(name = Estate.ADDRESS)
    @get:PropertyName(Estate.ADDRESS) @set:PropertyName(Estate.ADDRESS)
    var address: String? = null,

    @ColumnInfo(name = Estate.LOCALITY)
    @get:PropertyName(Estate.LOCALITY) @set:PropertyName(Estate.LOCALITY)
    var locality: String? = null,

    @ColumnInfo(name = Estate.CREATION_DATE_TS)
    @get:PropertyName(Estate.CREATION_DATE_TS) @set:PropertyName(Estate.CREATION_DATE_TS)
    var creationDateTs: Timestamp = Timestamp.now(),

    @ColumnInfo(name = Estate.DESCRIPTION)
    @get:PropertyName(Estate.DESCRIPTION) @set:PropertyName(Estate.DESCRIPTION)
    var description: String? = null,

    @ColumnInfo(name = Estate.PREVIEW_IMAGE_PATH)
    @get:PropertyName(Estate.PREVIEW_IMAGE_PATH) @set:PropertyName(Estate.PREVIEW_IMAGE_PATH)
    var previewImagePath: String? = null,

    @ColumnInfo(name = Estate.PRICE)
    @get:PropertyName(Estate.PRICE) @set:PropertyName(Estate.PRICE)
    var price: Long? = null,

    @ColumnInfo(name = Estate.ROOM_COUNT)
    @get:PropertyName(Estate.ROOM_COUNT) @set:PropertyName(Estate.ROOM_COUNT)
    var roomCount: Int? = null,

    @ColumnInfo(name = Estate.BATHROOM_COUNT)
    @get:PropertyName(Estate.BATHROOM_COUNT) @set:PropertyName(Estate.BATHROOM_COUNT)
    var bathroomCount: Int? = null,

    @ColumnInfo(name = Estate.BEDROOM_COUNT)
    @get:PropertyName(Estate.BEDROOM_COUNT) @set:PropertyName(Estate.BEDROOM_COUNT)
    var bedroomCount: Int? = null,

    @ColumnInfo(name = Estate.SALE_DATE_TS)
    @get:PropertyName(Estate.SALE_DATE_TS) @set:PropertyName(Estate.SALE_DATE_TS)
    var saleDateTs: Timestamp? = null,

    @ColumnInfo(name = Estate.SURFACE_AREA)
    @get:PropertyName(Estate.SURFACE_AREA) @set:PropertyName(Estate.SURFACE_AREA)
    var surfaceArea: Float? = null,

    @ColumnInfo(name = Estate.TYPE)
    @get:PropertyName(Estate.TYPE) @set:PropertyName(Estate.TYPE)
    var type: String? = null,

    @ColumnInfo(name = Estate.USER_ID)
    @get:PropertyName(Estate.USER_ID) @set:PropertyName(Estate.USER_ID)
    var userId: String? = null,

    @ColumnInfo(name = Estate.NEED_PUSH)
    @get:Exclude @set:Exclude
    var isPushNeeded: Boolean? = false
) {

    companion object {
        val DUMMY_ESTATES = listOf(
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
