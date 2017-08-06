package com.hubtele.android.helper

import com.google.gson.*
import timber.log.Timber
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object MyGson {
    val ISO_FORMAT = "yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'"
    val gson: Gson by lazy {
        GsonBuilder()
                .registerTypeAdapter(Date::class.java, DateDeserializer())
                .create()

    }

    class DateDeserializer : JsonDeserializer<Date> {

        override fun deserialize(element: JsonElement, arg1: Type, arg2: JsonDeserializationContext): Date? {
            val date = element.getAsString();

            val formatter = SimpleDateFormat(ISO_FORMAT);
            formatter.timeZone=TimeZone.getTimeZone("UTC");

            try {
                return formatter.parse(date);
            } catch (e: ParseException) {
                Timber.e("Failed to parse Date due to:", e);
                return null;
            }
        }
    }
}