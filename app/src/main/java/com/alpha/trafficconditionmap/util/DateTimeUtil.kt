package com.alpha.trafficconditionmap.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtil {

    fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    fun formatDateTime(input: String): String {
        val sdf: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val date: Date = sdf.parse(input)
        return SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(date)
    }
}
