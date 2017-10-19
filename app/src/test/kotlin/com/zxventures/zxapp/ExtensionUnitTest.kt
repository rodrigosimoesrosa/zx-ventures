package com.zxventures.zxapp

import com.zxventures.zxapp.extensions.toCurrency
import com.zxventures.zxapp.extensions.toISO8601
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.*


/**
 * Created by rodrigosimoesrosa
 */

@RunWith(MockitoJUnitRunner::class)
class ExtensionUnitTest {

    @Test
    fun testCurrency() {
        val value = 100.00.toCurrency(Locale("pt","BR"),
                Currency.getInstance(Locale("pt","BR")))
        val expected = "R$ 100,00"
        assertEquals("Conversion from to current has failed", expected, value)
    }

    @Test
    fun testISO8601() {
        val date = Date()
        date.time = 1504895270013

        val value = date.toISO8601()
        val expected = "2017-09-08T15:27Z"
        assertEquals("Conversion from date to iso8601 has failed", expected, value)
    }
}
