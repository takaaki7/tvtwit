package com.hubtele.android.util

import java.util.*

object DateUtil {
    fun diff(large: Date, small: Date): Date = Date(large.time - small.time)

    fun diffHour(date: Date, hour: Int): Date = Date(date.time - hour * 3600 * 1000)

    fun addHour(date: Date, hour: Int): Date = Date(date.time + hour * 3600 * 1000)
}