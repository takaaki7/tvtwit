package com.hubtele.android.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import com.hubtele.android.MyApplication

object ScreenUtil {

    fun width(ctx: Context): Int {
        return displayMetrics(ctx).widthPixels
    }

    fun height(ctx: Context): Int {
        return displayMetrics(ctx).heightPixels
    }

    fun dpToPx(ctx: Context, dp: Int): Int {
        val resources = ctx.resources
        val displayMetrics = resources!!.displayMetrics
        val scale = displayMetrics!!.density

        return ((dp * scale) + 0.5).toInt()
    }

    fun dpToPx(dp: Int): Int {
        val resources = MyApplication.getContext().resources
        val displayMetrics = resources!!.displayMetrics
        val scale = displayMetrics!!.density

        return ((dp * scale) + 0.5).toInt()
    }

    fun displayMetrics(ctx: Context): DisplayMetrics {
        val windowManager = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val metrics = DisplayMetrics()
        val display = windowManager.getDefaultDisplay()
        if (display == null)
            throw IllegalArgumentException("Default display must not be null!")

        display.getMetrics(metrics)

        return metrics
    }
}