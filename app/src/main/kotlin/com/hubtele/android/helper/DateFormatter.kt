package com.hubtele.android.helper

import java.text.SimpleDateFormat
import java.util.*
import javax.xml.datatype.Duration

object DateFormatter {
    val hmFormat by lazy {
        val sdf = SimpleDateFormat("HH:mm")
        sdf.timeZone = TimeZone.getTimeZone("Asia/Tokyo")
        sdf
    }
    fun toHmFormat(date: Date): String = hmFormat.format(date)

    val hmsFormat by lazy {
        val sdf = SimpleDateFormat("HH:mm:ss")
        sdf.timeZone = TimeZone.getTimeZone("Asia/Tokyo")
        sdf
    }

    fun toHmsFormat(date: Date): String = hmsFormat.format(date)

    val DURATION_FORMAT = "%d:%02d:%02d"
    fun formatDuration(milliSec: Long): String {
        val seconds = milliSec / 1000
        val positive = DURATION_FORMAT.format(seconds / 3600, (seconds % 3600) / 60, seconds % 60)
        return positive
    }

    val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    fun fromIsoFormat(str: String): Date = isoFormat.parse(str);

    val dateFormat by lazy {
        val sdf = SimpleDateFormat("MM/dd")
        sdf.timeZone = TimeZone.getTimeZone("Asia/Tokyo")
        sdf
    }

    fun toDateFormat(date: Date): String = dateFormat.format(date);

    val dateHmFormat by lazy {
        val sdf = SimpleDateFormat("MM/dd HH:mm")
        sdf.timeZone = TimeZone.getTimeZone("Asia/Tokyo")
        sdf
    }

    fun toDateHmFormat(date: Date): String = dateHmFormat.format(date);

}