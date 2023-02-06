package com.actiangent.note.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val localDateTimeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

fun todayDateTimeFormatted(): String = LocalDateTime.now().format(localDateTimeFormatter)