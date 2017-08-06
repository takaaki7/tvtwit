package com.hubtele.android.util

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtil {
    fun isOnline(ctx : Context) : Boolean {
        val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.getActiveNetworkInfo()
        return netInfo != null && netInfo.isConnectedOrConnecting()
    }
}