package com.openclassrooms.realestatemanager.utilsTest

import com.openclassrooms.realestatemanager.utils.IdUtils
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class IdUtilsTest {

    @Test
    fun `generateId with a specified length is really random`() {
        val length = 20
        val ids = arrayListOf<String>()

        for (i in 1..100) {
            val generatedId = IdUtils.generateId(length)
            assert(!ids.contains(generatedId))
            assert(generatedId.length == length)
            ids += generatedId
        }

    }

}