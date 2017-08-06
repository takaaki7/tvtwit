package com.hubtele.android.ui.helper

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.hubtele.android.MyApplication
import com.hubtele.android.util.ScreenUtil

/**
 * Created by nakama on 2015/11/05.
 */
class ProgramTableSpaceDecorator(
        val spanHeight: Int = ScreenUtil.dpToPx(10),
        val spanWidth: Int = ScreenUtil.dpToPx(4)) : RecyclerView.ItemDecoration() {


    override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        val pos = parent.getChildAdapterPosition(view)
        if (pos > 1) {
            outRect.top = spanHeight
        }
        if (pos < parent.adapter.itemCount - 2) {
            outRect.bottom = spanHeight
        }
        if (pos % 2 == 0) {
            outRect.right = spanWidth
        } else {
            outRect.left = spanWidth
        }
    }
}