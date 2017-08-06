package com.hubtele.android.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.hubtele.android.MyApplication
import java.util.*

object PrefUtil {

    private fun getPref(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext())
    }

    fun saveInt(key: String, value: Int) {
        getPref().edit().putInt(key, value).commit()
    }

    fun saveString(key: String, value: String) {
        getPref().edit().putString(key, value).commit()
    }

    fun loadString(key: String): String? {
        return getPref().getString(key,null)
    }

    fun saveDate(key: String, value: Date) {
        getPref().edit().putLong(key, value.time).commit();
    }

    fun saveBoolean(key: String, value: Boolean) {
        getPref().edit().putBoolean(key, value).commit();
    }

    //    fun loadString(key: String): String = getPref().getString(key, "")

    fun loadInt(key: String): Int = getPref().getInt(key, -1)

    fun loadDate(key: String): Date? {
        var milli = getPref().getLong(key, -1L);
        return if (milli != -1L) Date(milli) else null
    }

    fun loadBooleanDefFalse(key: String): Boolean = getPref().getBoolean(key, false)
    fun loadBooleanDefTrue(key: String): Boolean = getPref().getBoolean(key, true)

    fun remove(key: String) {
        getPref().edit().remove(key).commit()
    }

    fun clear() {
        getPref().edit().clear().commit()
    }
}
