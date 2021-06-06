package com.openclassrooms.realestatemanager.utilsTest

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.openclassrooms.realestatemanager.utils.Utils
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class InternetUtilTest {

    lateinit var connectivityManager: ConnectivityManager

    @Before
    fun setUp() {
        connectivityManager = findConnectivityManager()
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.M], manifest = Config.NONE)
    fun `Checking internet connection above Android M with success`() {
        val shadowNetworkCapabilities =
            Shadows.shadowOf(connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork))
        //Has Internet
        shadowNetworkCapabilities.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        Assert.assertTrue(Utils.isInternetAvailable(ApplicationProvider.getApplicationContext()))

        //Hasn't Internet
        shadowNetworkCapabilities.removeTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        shadowNetworkCapabilities.removeTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        shadowNetworkCapabilities.addTransportType(NetworkCapabilities.TRANSPORT_BLUETOOTH)
        Assert.assertFalse(Utils.isInternetAvailable(ApplicationProvider.getApplicationContext()))

    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.LOLLIPOP], manifest = Config.NONE)
    fun `Checking internet connection below Android M with success`() {
        val shadowNetworkInfo = Shadows.shadowOf(connectivityManager.activeNetworkInfo)
        //Has Internet
        shadowNetworkInfo.setConnectionType(ConnectivityManager.TYPE_MOBILE)
        Assert.assertTrue(Utils.isInternetAvailable(ApplicationProvider.getApplicationContext()))

        //Hasn't Internet
        shadowNetworkInfo.setConnectionType(ConnectivityManager.TYPE_BLUETOOTH)
        Assert.assertFalse(Utils.isInternetAvailable(ApplicationProvider.getApplicationContext()))

    }

    private fun findConnectivityManager() =
        ApplicationProvider.getApplicationContext<Context>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

}