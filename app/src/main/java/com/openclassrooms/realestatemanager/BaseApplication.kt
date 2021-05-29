package com.openclassrooms.realestatemanager

import androidx.multidex.MultiDexApplication
import androidx.room.Room
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.openclassrooms.realestatemanager.data.*
import com.openclassrooms.realestatemanager.data.local.AppDatabase
import com.openclassrooms.realestatemanager.data.remote.firebase.AppFirebaseHelper
import com.openclassrooms.realestatemanager.data.remote.firebase.FirebaseHelper
import com.openclassrooms.realestatemanager.data.remote.maps.AppLocationService
import com.openclassrooms.realestatemanager.data.remote.maps.LocationService
import com.openclassrooms.realestatemanager.data.remote.maps.MapsApi
import com.openclassrooms.realestatemanager.others.APP_DATABASE_NAME
import com.openclassrooms.realestatemanager.others.MAPS_API_BASE_URL
import com.openclassrooms.realestatemanager.ui.estatedetail.EstateDetailViewModel
import com.openclassrooms.realestatemanager.ui.estatelist.EstateListViewModel
import com.openclassrooms.realestatemanager.ui.login.LoginViewModel
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

        single {
            Room.databaseBuilder(get(), AppDatabase::class.java, APP_DATABASE_NAME)
                .build()
        }

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

        single<LocationService> {
            AppLocationService(get())
        }

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

        single<FirebaseHelper> {AppFirebaseHelper(get())}

        single<MapsRepository> { AppMapsRepository(get(), get(), get()) }
        single<UserRepository> { AppUserRepository(get(), get(), get()) }
        single<EstateRepository> {
            AppEstateRepository(get(), CoroutineScope(SupervisorJob() + Dispatchers.Main), get(), get(), get())
        }

        worker { SyncWorker(get(), get(), get(), get(), get()) }

        viewModel { EstateListViewModel(get()) }
        viewModel { EstateDetailViewModel(get(), get()) }
        viewModel { LoginViewModel(get()) }

    }

    @KoinExperimentalAPI
    override fun onCreate() {
        super.onCreate()
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