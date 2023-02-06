package com.actiangent.note.util

import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class LocalDateTimeExtensionTest {

    private val dateTimePatternFormat = "yyyy-MM-dd HH:mm"

    @Test
    fun dateTimeFormat_verifyTrue() {
        val todayDateTime = todayDateTimeFormatted()

        assertTrue(isDateTimeValid(dateTimePatternFormat, todayDateTime))
    }

    private fun isDateTimeValid(format: String, dateTime: String): Boolean {
        return try {
            LocalDateTime.parse(
                dateTime,
                DateTimeFormatter.ofPattern(format)
            )
            true
        } catch (exception: DateTimeParseException) {
            false
        }
    }

}