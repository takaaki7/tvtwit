package com.hubtele.android.ui.view

import android.content.Context
import android.graphics.PointF
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.TypedValue

class ChatLayoutManager(context: Context, val speed/*smaller is faster. Msec to spend scrolling 1px*/: Float = 4F) : LinearLayoutManager(context) {

    override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?,
                                        position: Int) {
        val linearSmoothScroller = object : LinearSmoothScroller(recyclerView!!.context) {
            override fun computeScrollVectorForPosition(targetPosition: Int): PointF {
                return this@ChatLayoutManager.computeScrollVectorForPosition(targetPosition)
            }

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                return speed / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1F, displayMetrics);
            }
        }
        linearSmoothScroller.targetPosition = position
        startSmoothScroll(linearSmoothScroller)
    }
}