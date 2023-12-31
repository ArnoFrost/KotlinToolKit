package com.arno.tech.toolkit.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


object DateUtils {
    fun getCurrentFormattedDate(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return current.format(formatter)
    }
}
