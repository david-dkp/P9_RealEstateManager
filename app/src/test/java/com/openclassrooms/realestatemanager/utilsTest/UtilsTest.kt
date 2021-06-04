package com.openclassrooms.realestatemanager.utilsTest

import com.openclassrooms.realestatemanager.utils.Utils
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class UtilsTest {

    @Test
    fun `convert euros to dollars`() {
        val euros = 50
        val expectedDollars = 61

        assert(Utils.convertEuroToDollar(euros) == expectedDollars)
    }

    @Test
    fun `convert dollars to euros`() {
        val dollars = 784
        val expectedEuros = 646

        assert(Utils.convertDollarToEuro(dollars) == expectedEuros)
    }

    @Test
    fun `getTodayDate() with correct format`() {
        val calendar = Calendar.getInstance()
        val expectedFormat = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH)}/${calendar.get(Calendar.YEAR)}"

        assert(Utils.getTodayDate() == expectedFormat)
    }
}