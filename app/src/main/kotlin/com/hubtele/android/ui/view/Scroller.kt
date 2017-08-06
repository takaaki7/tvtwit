package com.hubtele.android.ui.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.LinearLayout
import android.widget.LinearLayout.*
import com.hubtele.android.R
import kotlinx.android.synthetic.main.scroller.view.*

class Scroller : LinearLayout {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize(context)
    }

    private fun initialize(context: Context) {
        orientation = HORIZONTAL
        clipChildren = false
        LayoutInflater.from(context).inflate(R.layout.scroller, this)
    }

    private val ANIME_DURATION = 200L
    private val SCALE_X = "scaleX"
    private val SCALE_Y = "scaleY"
    private val ALPHA = "alpha"

    private fun showHandle() {
        val animatorSet = AnimatorSet()
        scrollerHandle.pivotX = scrollerHandle.width.toFloat()
        scrollerHandle.pivotY = scrollerHandle.height.toFloat()
        scrollerHandle.visibility = VISIBLE
        val growerX = ObjectAnimator.ofFloat(scrollerHandle, SCALE_X, 0f, 1f).setDuration(ANIME_DURATION)
        val growerY = ObjectAnimator.ofFloat(scrollerHandle, SCALE_Y, 0f, 1f).setDuration(ANIME_DURATION)
        val alpha = ObjectAnimator.ofFloat(scrollerHandle, ALPHA, 0f, 1f).setDuration(ANIME_DURATION)
        animatorSet.playTogether(growerX, growerY, alpha)
        animatorSet.start()
    }

    private var currentAnimator: AnimatorSet? = null

    private fun hideHandle() {
        var ca: AnimatorSet? = AnimatorSet()
        scrollerHandle.pivotX = scrollerHandle.width.toFloat()
        scrollerHandle.pivotY = scrollerHandle.height.toFloat()
        val shrinkerX = ObjectAnimator.ofFloat(scrollerHandle, SCALE_X, 1f, 0f).setDuration(ANIME_DURATION)
        val shrinkerY = ObjectAnimator.ofFloat(scrollerHandle, SCALE_Y, 1f, 0f).setDuration(ANIME_DURATION)
        val alpha = ObjectAnimator.ofFloat(scrollerHandle, ALPHA, 1f, 0f).setDuration(ANIME_DURATION)
        ca?.playTogether(shrinkerX, shrinkerY, alpha)
        ca?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                scrollerHandle.visibility = INVISIBLE
                ca = null
            }

            override fun onAnimationCancel(animation: Animator?) {
                super.onAnimationCancel(animation)
                scrollerHandle.visibility = INVISIBLE
                ca = null
            }
        })
        ca?.start()
    }

    private var scrollerHeight: Int = 0
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        scrollerHeight = h
    }

    private fun setPosition(y: Float) {
        val position: Float = y / scrollerHeight;
//        val bubbleHeight: Int = scrollerBubble.height;
//        scrollerBubble.y = getValueInRangeFloat(0F, (scrollerHeight - bubbleHeight).toFloat(), (scrollerHeight - bubbleHeight) * position);
        val handleHeight: Int = scrollerHandle.height;
        scrollerHandle.y = getValueInRangeFloat(0F, (scrollerHeight - handleHeight).toFloat(), (scrollerHeight - handleHeight) * position)
    }

    private fun getValueInRangeFloat(min: Float, max: Float, value: Float): Float {
        val minimum = Math.max(min, value);
        return Math.min(minimum, max);
    }

    private fun getValueInRange(min: Int, max: Int, value: Int): Int {
        val minimum = Math.max(min, value);
        return Math.min(minimum, max);
    }

    var layoutManager: ChatLayoutManager? = null
    var recyclerView: RecyclerView? = null
        set(v) {
            field = v
            layoutManager = field?.layoutManager as ChatLayoutManager
            field?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                    val firstVisibleView = rv.getChildAt(0)
                    val firstVisiblePosition = rv.getChildLayoutPosition(firstVisibleView)
                    val visibleRange = rv.childCount
                    val lastVisiblePosition = firstVisiblePosition + visibleRange
                    val itemCount = rv.adapter.itemCount
                    val position: Int
                    if (firstVisiblePosition == 0) {
                        position = 0
                    } else if (lastVisiblePosition == itemCount - 1) {
                        position = itemCount - 1
                    } else {
                        position = firstVisiblePosition
                    }
                    val proportion = position.toFloat() / itemCount.toFloat()
                    setPosition(height * proportion)
                }
            })
        }


    private val handleHider = HandleHider()

    private inner class HandleHider : Runnable {
        override fun run() = hideHandle()
    }

    interface OnUserDragListener {
        fun onDraggingStarted()
        fun onDraggingStopped()
    }

    var userDragListener: OnUserDragListener? = null

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action === MotionEvent.ACTION_DOWN || event.action === MotionEvent.ACTION_MOVE) {
            if (event.action === MotionEvent.ACTION_DOWN) userDragListener?.onDraggingStarted()
            setPosition(event.y)
            if (currentAnimator != null) currentAnimator?.cancel()
            handler.removeCallbacks(handleHider)
            if (scrollerHandle.visibility === INVISIBLE) showHandle()
            setRecyclerViewPosition(event.y)
            return true
        } else if (event.action === MotionEvent.ACTION_UP) {
            userDragListener?.onDraggingStopped()
            handler.postDelayed(handleHider, HANDLE_HIDE_DELAY)
            return true
        }
        return super.onTouchEvent(event)
    }

    private fun setRecyclerViewPosition(y: Float) {
        if (recyclerView != null) {
            val itemCount = recyclerView!!.adapter.itemCount
            val proportion: Float
            if (scrollerHandle.y == 0F) {
                proportion = 0f
            } else if (scrollerHandle.y + scrollerHandle.height >= scrollerHeight - TRACK_SNAP_RANGE) {
                proportion = 1f
            } else {
                proportion = y / scrollerHeight
            }
            val targetHeight = proportion * itemCount;
            val targetPos = getValueInRange(0, itemCount - 1, (proportion * itemCount).toInt())
            val targetOffset: Int =
                    if (targetPos in arrayOf(0, itemCount - 1)) 0
                    else ((targetHeight - targetPos) * (recyclerView!!.getChildAt(0)?.height ?: 0)/*about average row height*/).toInt()
            layoutManager!!.scrollToPositionWithOffset(targetPos, -targetOffset)
            //            recyclerView!!.scrollToPosition(targetPos)
        }
    }

    companion object {
        private val HANDLE_HIDE_DELAY = 1000L
        private val TRACK_SNAP_RANGE = 5
    }
}