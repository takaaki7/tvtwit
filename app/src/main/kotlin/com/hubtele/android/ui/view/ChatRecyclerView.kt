package com.hubtele.android.ui.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.hubtele.android.R
import kotlinx.android.synthetic.main.activity_chat.recyclerView

class ChatRecyclerView : RecyclerView {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize(context)
    }

    private fun initialize(context: Context) {
    }

    fun shouldAutoScroll(): Boolean {
        return getLinearLayoutManager().isSmoothScrolling || adapter.itemCount - 1 == getLinearLayoutManager().findLastCompletelyVisibleItemPosition()
    }

    fun shouldAutoScrollOnEmitted():Boolean{
        return getLinearLayoutManager().isSmoothScrolling || adapter.itemCount - 2 == getLinearLayoutManager().findLastCompletelyVisibleItemPosition()
    }

    fun scrollIfNeeded() {
        if (shouldAutoScrollOnEmitted()) smoothScrollToPosition(adapter.itemCount)
    }
    fun scrollToBottom(){
        smoothScrollToPosition(adapter.itemCount)
    }
    private  var linearLayoutManager: LinearLayoutManager?=null

    fun getLinearLayoutManager(): LinearLayoutManager {
        if (linearLayoutManager == null) {
            linearLayoutManager = layoutManager as LinearLayoutManager
        }
        return linearLayoutManager!!
    }
}