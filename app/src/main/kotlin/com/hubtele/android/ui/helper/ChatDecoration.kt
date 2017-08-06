package com.hubtele.android.ui.helper

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import com.hubtele.android.R
import com.hubtele.android.ui.view.ChatRecyclerView
import com.hubtele.android.util.ScreenUtil

class ChatDecoration(context: Context) : BoardDecoration(context) {

    private lateinit var lastDivider: Drawable;
    private val lastDividerHeight = ScreenUtil.dpToPx(context, 2)

    init {
        lastDivider = ContextCompat.getDrawable(context, R.drawable.chat_last_divider)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        drawLastDivider(c, parent as ChatRecyclerView)
    }

    fun drawLastDivider(c: Canvas, parent: ChatRecyclerView) {

        if (parent.shouldAutoScroll()) {
            lastDivider.setBounds(0, parent.bottom - lastDividerHeight, parent.width, parent.bottom);
            lastDivider.draw(c)
        }
    }


}