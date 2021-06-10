package com.openclassrooms.realestatemanager.viewmodelsTest

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.firebase.Timestamp
import com.openclassrooms.realestatemanager.data.EstateRepository
import com.openclassrooms.realestatemanager.data.MapsRepository
import com.openclassrooms.realestatemanager.data.SelectedEstateRepository
import com.openclassrooms.realestatemanager.data.UserRepository
import com.openclassrooms.realestatemanager.data.models.domain.Estate
import com.openclassrooms.realestatemanager.data.models.domain.User
import com.openclassrooms.realestatemanager.data.models.responses.GeocodingResponse
import com.openclassrooms.realestatemanager.data.models.responses.NearbySearchResponse
import com.openclassrooms.realestatemanager.others.Resource
import com.openclassrooms.realestatemanager.ui.estatelist.EstateFilterData
import com.openclassrooms.realestatemanager.ui.estatelist.EstateListViewModel
import com.openclassrooms.realestatemanager.utils.getOrAwaitValue
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner.Silent::class)
class EstateListViewModelTest {

    @ObsoleteCoroutinesApi
    private val mainThread = newSingleThreadContext("Solo thread")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var estateRepository: EstateRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var mapsRepository: MapsRepository

    @Mock
    private lateinit var selectedEstateRepository: SelectedEstateRepository

    private lateinit var viewModel: EstateListViewModel

    private lateinit var repoEstatesFlow: MutableStateFlow<List<Estate>>

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        runBlocking {
            Dispatchers.setMain(mainThread)
            repoEstatesFlow = MutableStateFlow(listOf())
            `when`(userRepository.isLoggedInFlow()).thenReturn(flow { true })
            `when`(userRepository.getCurrentUser()).thenReturn(Resource.Success(User()))
            `when`(estateRepository.getEstatesFlow()).thenReturn(repoEstatesFlow)
            `when`(estateRepository.refreshEstates()).thenReturn(Resource.Success())
            `when`(selectedEstateRepository.getSelectedEstateId()).thenReturn(MutableLiveData())
            viewModel = EstateListViewModel(
                context,
                estateRepository,
                userRepository,
                mapsRepository,
                selectedEstateRepository
            )
        }
    }

    @Test
    fun filterEstatesTest() {
        runBlocking {

            `when`(mapsRepository.getGeocodingResult("First address")).thenReturn(
                Resource.Success(
                    GeocodingResponse.Result(
                        "", listOf(),
                        GeocodingResponse.Geometry(GeocodingResponse.Location(0f, 0f))
                    )
                )
            )
            `when`(mapsRepository.getGeocodingResult("Second address")).thenReturn(
                Resource.Success(
                    GeocodingResponse.Result(
                        "", listOf(),
                        GeocodingResponse.Geometry(GeocodingResponse.Location(1f, 1f))
                    )
                )
            )
            `when`(mapsRepository.getGeocodingResult("Third address")).thenReturn(
                Resource.Success(
                    GeocodingResponse.Result(
                        "", listOf(),
                        GeocodingResponse.Geometry(GeocodingResponse.Location(2f, 2f))
                    )
                )
            )
            `when`(mapsRepository.getGeocodingResult("Fourth address")).thenReturn(
                Resource.Success(
                    GeocodingResponse.Result(
                        "", listOf(),
                        GeocodingResponse.Geometry(GeocodingResponse.Location(3f, 3f))
                    )
                )
            )

            `when`(mapsRepository.getNearbyResults(LatLng(0.0, 0.0))).thenReturn(
                Resource.Success(
                    listOf(
                        NearbySearchResponse.Result(
                            NearbySearchResponse.Geometry(NearbySearchResponse.Location(0f, 0f)),
                            listOf("school")
                        ),
                    )
                )
            )

            `when`(mapsRepository.getNearbyResults(LatLng(1.0, 1.0))).thenReturn(
                Resource.Success(
                    listOf(
                        NearbySearchResponse.Result(
                            NearbySearchResponse.Geometry(NearbySearchResponse.Location(1f, 1f)),
                            listOf("restaurant")
                        ),
                    )
                )
            )

            `when`(mapsRepository.getNearbyResults(LatLng(2.0, 2.0))).thenReturn(
                Resource.Success(
                    listOf(
                        NearbySearchResponse.Result(
                            NearbySearchResponse.Geometry(NearbySearchResponse.Location(2f, 2f)),
                            listOf("store")
                        ),
                    )
                )
            )

            `when`(mapsRepository.getNearbyResults(LatLng(3.0, 3.0))).thenReturn(
                Resource.Success(
                    listOf(
                        NearbySearchResponse.Result(
                            NearbySearchResponse.Geometry(NearbySearchResponse.Location(3f, 3f)),
                            listOf("store")
                        ),
                    )
                )
            )

            repoEstatesFlow.value = listOf(
                Estate(
                    "1",
                    "First address",
                    "California",
                    Timestamp(Date(1)),
                    "Some desc",
                    "Image path",
                    5,
                    10000,
                    10,
                    2,
                    7,
                    Timestamp.now(),
                    3,
                    "First Type",
                    "1",
                    false
                ),
                Estate(
                    "2",
                    "Second address",
                    "California",
                    Timestamp(Date(3)),
                    "Some desc",
                    "Image path",
                    2,
                    7800,
                    4,
                    2,
                    2,
                    Timestamp.now(),
                    50,
                    "First Type",
                    "2",
                    false
                ),
                Estate(
                    "3",
                    "Third address",
                    "California",
                    Timestamp(Date(1)),
                    "Some desc",
                    "Image path",
                    7,
                    28754,
                    4,
                    1,
                    3,
                    null,
                    90,
                    "I-House",
                    "3",
                    false
                ),
                Estate(
                    "4",
                    "Fourth address",
                    "California",
                    Timestamp(Date(2)),
                    "Some desc",
                    "Image path",
                    0,
                    19000,
                    6,
                    1,
                    5,
                    Timestamp.now(),
                    41,
                    "Gablefront",
                    "4",
                    false
                ),
            )

            val expectedOutput = listOf(
                repoEstatesFlow.value.last()
            )

            val filterData = EstateFilterData(
                LongRange(15000, 30000),
                listOf(Place.Type.STORE),
                IntRange(40, 100),
                "Gablefront"
            )

            viewModel.setFilter(filterData)

            assert(viewModel.estates.getOrAwaitValue().also { println(it) } == expectedOutput)

        }

    }
}