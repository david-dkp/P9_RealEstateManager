package com.openclassrooms.realestatemanager.providers

import android.content.Context
import android.net.Uri
import android.test.ProviderTestCase2
import android.test.mock.MockContentResolver
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.Timestamp
import com.openclassrooms.realestatemanager.contracts.AppDatabaseContract
import com.openclassrooms.realestatemanager.data.local.AppDatabase
import com.openclassrooms.realestatemanager.data.models.domain.Estate
import com.openclassrooms.realestatemanager.data.models.domain.EstateImage
import com.openclassrooms.realestatemanager.data.models.domain.User
import com.openclassrooms.realestatemanager.others.APP_DATABASE_NAME
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class EstateContentProviderTest : ProviderTestCase2<EstateContentProvider>(
    EstateContentProvider::class.java,
    AppDatabaseContract.AUTHORITY
) {

    private lateinit var mockResolver: MockContentResolver
    private lateinit var appDatabase: AppDatabase

    override fun getContext(): Context {
        return ApplicationProvider.getApplicationContext()
    }

    @Before
    fun setup() {
        super.setUp()
        mockResolver = mockContentResolver
        appDatabase =
            Room.databaseBuilder(mockContext, AppDatabase::class.java, APP_DATABASE_NAME).build()
    }

    @Test
    fun getEstatesWithSuccess(): Unit = runBlocking {

        val expectedOutput = listOf(
            Estate(
                "sdfggs",
                creationDateTs = Timestamp(Date(455143)),
                description = "desc",
                photoCount = 3,
                bedroomCount = 4
            ),
            Estate(
                "rzfeg",
                creationDateTs = Timestamp(Date(1231)),
                description = "more desc",
                photoCount = 6,
                bedroomCount = 2
            ),
            Estate(
                "zegsg",
                creationDateTs = Timestamp(Date(456345)),
                description = "even more desc",
                photoCount = 7,
                bedroomCount = 7
            ),
        )

        appDatabase.estateDao().insertAllEstates(expectedOutput)

        var cursor = mockResolver.query(
            AppDatabaseContract.Estate.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        assert(cursor != null)

        while (cursor?.moveToNext() == true) {
            cursor.apply {
                val idIndex = getColumnIndex(AppDatabaseContract.Estate.ID)
                val creationDateTsIndex =
                    getColumnIndex(AppDatabaseContract.Estate.CREATION_DATE_TS)
                val descriptionIndex = getColumnIndex(AppDatabaseContract.Estate.DESCRIPTION)
                val photoCountIndex = getColumnIndex(AppDatabaseContract.Estate.PHOTO_COUNT)
                val bedroomCountIndex = getColumnIndex(AppDatabaseContract.Estate.BEDROOM_COUNT)

                val estate = Estate(
                    getString(idIndex),
                    creationDateTs = Timestamp(Date(getLong(creationDateTsIndex))),
                    description = getString(descriptionIndex),
                    photoCount = getInt(photoCountIndex),
                    bedroomCount = getInt(bedroomCountIndex)
                )

                assert(expectedOutput.contains(estate))
            }
        }

        cursor = mockResolver.query(
            Uri.parse(AppDatabaseContract.Estate.CONTENT_URI.toString() + "/" + expectedOutput.first().id),
            null,
            null,
            null,
            null
        )

        assert(cursor != null)

        cursor?.apply {
            cursor.moveToFirst()
            val idIndex = getColumnIndex(AppDatabaseContract.Estate.ID)
            val creationDateTsIndex = getColumnIndex(AppDatabaseContract.Estate.CREATION_DATE_TS)
            val descriptionIndex = getColumnIndex(AppDatabaseContract.Estate.DESCRIPTION)
            val photoCountIndex = getColumnIndex(AppDatabaseContract.Estate.PHOTO_COUNT)
            val bedroomCountIndex = getColumnIndex(AppDatabaseContract.Estate.BEDROOM_COUNT)

            val estate = Estate(
                getString(idIndex),
                creationDateTs = Timestamp(Date(getLong(creationDateTsIndex))),
                description = getString(descriptionIndex),
                photoCount = getInt(photoCountIndex),
                bedroomCount = getInt(bedroomCountIndex)
            )

            assert(estate == expectedOutput.first())
        }

    }

    @Test
    fun getEstatesImagesWithSuccess(): Unit = runBlocking {

        val expectedOutput = listOf(
            EstateImage("qdf4541qf", description = "Awesome description"),
            EstateImage("qd4848qf", description = "Newbie description"),
            EstateImage("s3df2fs2df54adf", description = "Ok description"),
            EstateImage("q41fd4q5", description = "Why not description"),
        )

        appDatabase.estateImageDao().insertAllEstateImages(expectedOutput)

        var cursor = mockResolver.query(
            AppDatabaseContract.EstateImage.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        while (cursor?.moveToNext() == true) {
            cursor.apply {
                val idIndex = getColumnIndex(AppDatabaseContract.EstateImage.ID)
                val descriptionIndex = getColumnIndex(AppDatabaseContract.EstateImage.DESCRIPTION)

                val estateImage = EstateImage(
                    getString(idIndex),
                    description = getString(descriptionIndex),
                )

                assert(expectedOutput.contains(estateImage))
            }
        }

        cursor = mockResolver.query(
            Uri.parse(AppDatabaseContract.EstateImage.CONTENT_URI.toString() + "/" + expectedOutput.first().id),
            null,
            null,
            null,
            null
        )

        assert(cursor != null)

        cursor?.apply {
            cursor.moveToFirst()
            val idIndex = getColumnIndex(AppDatabaseContract.EstateImage.ID)
            val descriptionIndex = getColumnIndex(AppDatabaseContract.EstateImage.DESCRIPTION)

            val estateImage = EstateImage(
                getString(idIndex),
                getString(descriptionIndex)
            )

            assert(estateImage == expectedOutput.first())
        }

    }

    @Test
    fun getUsersWithSuccess(): Unit = runBlocking {

        val expectedOutput = listOf(
            User("sdfggs", email = "firstpeople@gmail.com", firstName = "FirstNameFirst"),
            User("sdfsqdff", email = "secondone@gmail.com", firstName = "July"),
            User("qezfxfsfdg", email = "thirdone@gmail.com", firstName = "Rottie"),
        )

        appDatabase.userDao().insertAllUsers(expectedOutput)

        var cursor = mockResolver.query(
            AppDatabaseContract.User.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        while (cursor?.moveToNext() == true) {
            val idIndex = cursor.getColumnIndex(AppDatabaseContract.User.ID)
            val emailIndex = cursor.getColumnIndex(AppDatabaseContract.User.EMAIL)
            val firstNameIndex = cursor.getColumnIndex(AppDatabaseContract.User.FIRST_NAME)

            val user = User(
                cursor.getString(idIndex),
                email = cursor.getString(emailIndex),
                firstName = cursor.getString(firstNameIndex)
            )

            assert(expectedOutput.contains(user))
        }

        cursor = mockResolver.query(
            Uri.parse(AppDatabaseContract.User.CONTENT_URI.toString() + "/" + expectedOutput.first().id),
            null,
            null,
            null,
            null
        )

        assert(cursor != null)

        cursor?.apply {
            cursor.moveToFirst()
            val idIndex = cursor.getColumnIndex(AppDatabaseContract.User.ID)
            val emailIndex = cursor.getColumnIndex(AppDatabaseContract.User.EMAIL)
            val firstNameIndex = cursor.getColumnIndex(AppDatabaseContract.User.FIRST_NAME)

            val user = User(
                getString(idIndex),
                email = getString(emailIndex),
                firstName = getString(firstNameIndex)
            )

            assert(user == expectedOutput.first())
        }

    }


    @After
    fun teardown() {
        super.tearDown()
    }

}