package com.openclassrooms.realestatemanager

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class BaseApplication : MultiDexApplication() {

    private val appModule = module {

    }

    override fun onCreate() {
        super.onCreate()

        Firebase.firestore.firestoreSettings = firestoreSettings {
            isPersistenceEnabled = false
        }

        startKoin {
            androidLogger()
            androidContext(this@BaseApplication)
            modules(appModule)
        }
    }

}