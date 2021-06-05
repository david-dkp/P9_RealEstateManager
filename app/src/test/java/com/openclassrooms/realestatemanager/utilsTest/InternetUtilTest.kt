package com.openclassrooms.realestatemanager.utilsTest

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.openclassrooms.realestatemanager.BaseApplication
import com.openclassrooms.realestatemanager.utils.Utils
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowNetworkCapabilities

@RunWith(RobolectricTestRunner::class)
class InternetUtilTest {

    lateinit var connectivityManager: ConnectivityManager
    lateinit var shadowNetworkCapabilities: ShadowNetworkCapabilities

    @Before
    fun setUp() {
        connectivityManager = findConnectivityManager()
        shadowNetworkCapabilities =
            Shadows.shadowOf(connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork))
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.M])
    fun `Checking internet connection above Android M with success`() {
        //Has Internet
        shadowNetworkCapabilities.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        assert(Utils.isInternetAvailable(ApplicationProvider.getApplicationContext()) == true)

        //Hasn't Internet
        shadowNetworkCapabilities.removeTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        shadowNetworkCapabilities.addTransportType(NetworkCapabilities.TRANSPORT_BLUETOOTH)

        assert(Utils.isInternetAvailable(ApplicationProvider.getApplicationContext()) == false)

    }

    private fun findConnectivityManager() =
        ApplicationProvider.getApplicationContext<BaseApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

}