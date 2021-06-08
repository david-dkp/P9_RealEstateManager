package com.openclassrooms.realestatemanager

import androidx.multidex.MultiDexApplication
import androidx.room.Room
import androidx.work.WorkerParameters
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.openclassrooms.realestatemanager.data.*
import com.openclassrooms.realestatemanager.data.local.AppDatabase
import com.openclassrooms.realestatemanager.data.remote.firebase.AppFirebaseHelper
import com.openclassrooms.realestatemanager.data.remote.firebase.FirebaseHelper
import com.openclassrooms.realestatemanager.data.remote.maps.*
import com.openclassrooms.realestatemanager.others.APP_DATABASE_NAME
import com.openclassrooms.realestatemanager.others.MAPS_API_BASE_URL
import com.openclassrooms.realestatemanager.ui.addestate.AddEstateViewModel
import com.openclassrooms.realestatemanager.ui.estatedetail.EstateDetailViewModel
import com.openclassrooms.realestatemanager.ui.estatelist.EstateListViewModel
import com.openclassrooms.realestatemanager.ui.login.LoginViewModel
import com.openclassrooms.realestatemanager.ui.map.MapViewModel
import com.openclassrooms.realestatemanager.workers.SyncWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.KoinExperimentalAPI
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BaseApplication : MultiDexApplication(), KoinComponent {

    private val appModule = module {

        //Db
        single {
            Room.databaseBuilder(get(), AppDatabase::class.java, APP_DATABASE_NAME)
                .build()
        }

        //Daos
        single {
            val db: AppDatabase = get()
            db.estateDao()
        }

        single {
            val db: AppDatabase = get()
            db.estateImageDao()
        }

        single {
            val db: AppDatabase = get()
            db.userDao()
        }

        //Apis
        single {
            Retrofit.Builder()
                .baseUrl(MAPS_API_BASE_URL)
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder()
                            .excludeFieldsWithoutExposeAnnotation()
                            .create()
                    )
                )
                .build()
                .create(MapsApi::class.java)
        }
        single<LocationService> { AppLocationService(get()) }

        //Caches
        single { GeocodingCache() }
        single { NearbySearchCache() }
        single { LocationCache(get()) }

        //Repos
        single<FirebaseHelper> { AppFirebaseHelper(get()) }
        single<MapsRepository> { AppMapsRepository(get(), get(), get(), get(), get(), get()) }
        single<UserRepository> { AppUserRepository(get(), get(), get()) }
        single<EstateRepository> {
            AppEstateRepository(
                get(),
                CoroutineScope(SupervisorJob() + Dispatchers.Main),
                get(),
                get(),
                get()
            )
        }
        single<SelectedEstateRepository> { AppSelectedEstateRepository() }

        //ViewModels
        viewModel { EstateListViewModel(get(), get(), get(), get(), get()) }
        viewModel { EstateDetailViewModel(get(), get(), get()) }
        viewModel { LoginViewModel(get()) }
        viewModel { (id: String) -> AddEstateViewModel(get(), id, get()) }
        viewModel { MapViewModel(get(), get()) }

        //Workers
        worker { (workerParams: WorkerParameters) ->
            SyncWorker(get(), get(), get(), get(), get(), workerParams)
        }

    }

    @KoinExperimentalAPI
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        Firebase.firestore.firestoreSettings = firestoreSettings {
            isPersistenceEnabled = false
        }

        startKoin {
            androidLogger()
            androidContext(this@BaseApplication)
            workManagerFactory()
            modules(appModule)
        }
    }

}