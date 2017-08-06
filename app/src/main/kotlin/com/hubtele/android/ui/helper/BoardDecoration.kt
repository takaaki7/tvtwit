package com.hubtele.android.ui.helper

import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.hubtele.android.R
import com.hubtele.android.ui.view.ChatRecyclerView
import com.hubtele.android.util.ScreenUtil
import timber.log.Timber

open class BoardDecoration(val context: Context) : RecyclerView.ItemDecoration () {
    private var ATTRS = intArrayOf(android.R.attr.listDivider)

    private lateinit var divider: Drawable;
    private val dividerWidth = ScreenUtil.dpToPx(context, 64)

    init {
        val a = context.obtainStyledAttributes(ATTRS);
        divider = a.getDrawable(0)
        a.recycle()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        drawHorizontal(c, parent as ChatRecyclerView);
    }

    private fun drawHorizontal(c: Canvas, parent: ChatRecyclerView) {
        val right = parent.width - parent.paddingRight;
        val lastVisiblePosition = parent.childCount - 1;
        for (i in 0..lastVisiblePosition-1) {
            val child = parent.getChildAt(i);
            val param = child.layoutParams as RecyclerView.LayoutParams;
            val top = child.bottom + param.bottomMargin
            //                var bottom = top + mLastDivider.intrinsicHeight
            val bottom = top + divider.intrinsicHeight;
            divider.setBounds(dividerWidth, top, right, bottom);
            divider.draw(c);
        }

    }

    private fun isAdapterLastView(view: View?, recView: RecyclerView): Boolean {
        if (view != null) {
            return recView.getChildAdapterPosition(view) == recView.adapter.itemCount - 1
        } else {
            return false
        }
    }

    override fun getItemOffsets(outRect: Rect?, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        if (isAdapterLastView(view, parent)) {
//            outRect?.set(0, 0, 0, lastDividerHeight)
        } else {
            outRect?.set(0, 0, 0, divider.intrinsicHeight)
        }
    }
}