package com.hubtele.android.ui.helper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View

class RankingListDividerDecoration(val context: Context) : RecyclerView.ItemDecoration () {
    private var ATTRS = intArrayOf(android.R.attr.listDivider)

    private lateinit var divider: Drawable;

    init {
        val a = context.obtainStyledAttributes(ATTRS);
        divider = a.getDrawable(0)
        a.recycle()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        drawVertical(c, parent);
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft;
        val right = parent.width - parent.paddingRight;
        val childCount = parent.childCount;
        for (i in 0..childCount-2) {
            val child = parent.getChildAt(i);
            val param = child.layoutParams as RecyclerView.LayoutParams;
            val top = child.bottom + param.bottomMargin
            val bottom = top + divider.intrinsicHeight;
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        outRect?.set(0, 0, 0, divider.intrinsicHeight)
    }
}