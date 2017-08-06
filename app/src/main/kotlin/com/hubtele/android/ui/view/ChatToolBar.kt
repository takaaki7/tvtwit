package com.hubtele.android.ui.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import com.hubtele.android.R
import kotlinx.android.synthetic.main.chat_toolbar.view.*
import timber.log.Timber

class ChatToolBar : Toolbar {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize(context)
    }

    private fun initialize(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.chat_toolbar, this, true)
        setContentInsetsAbsolute(0, 0)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        handler.postDelayed(barHider, HIDE_DELAY)
    }

    override fun setTitle(title: CharSequence?) {
        //        super.setTitle(title)
        toolbarTitle.text = title.toString()
    }

    override fun setSubtitle(subtitle: CharSequence?) {
        //        super.setSubtitle(resId)
        toolbarSubTitle.text = subtitle
    }

    private val TRANSLATION_Y = "translationY"
    private val ANIME_DURATION = 300L
    private fun showToolbar() {
        Timber.d("showToolbar height:${-toolbar.height.toFloat()}");
        val animatorSet = AnimatorSet()
        visibility = VISIBLE
        var translateY = ObjectAnimator.ofFloat(toolbar, TRANSLATION_Y, -height.toFloat(), 0F).setDuration(ANIME_DURATION)
        animatorSet.play(translateY)
        animatorSet.start()
    }

    private var currentAnimator: AnimatorSet? = null
    private fun hideToolbar() {
        currentAnimator = AnimatorSet()
        val translateY = ObjectAnimator.ofFloat(toolbar, TRANSLATION_Y, 0F, -height.toFloat() - 2).setDuration(ANIME_DURATION)
        currentAnimator?.play(translateY)
        currentAnimator?.addListener(object : AnimatorListenerAdapter() {
            val makeInvisible = {
                visibility = INVISIBLE
                currentAnimator = null
            }

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                makeInvisible()
            }

            override fun onAnimationCancel(animation: Animator?) {
                super.onAnimationCancel(animation)
                makeInvisible()
            }
        })
        currentAnimator?.start()
    }

    private val barHider = BarHider()

    private inner class BarHider : Runnable {
        override fun run() = hideToolbar()
    }

    private val HIDE_DELAY = 1500L
    fun onScreenTouched(event: MotionEvent) {
        if (event.action === MotionEvent.ACTION_DOWN || event.action === MotionEvent.ACTION_MOVE) {
            if (currentAnimator != null) currentAnimator?.cancel()
            handler.removeCallbacks(barHider)
            if (visibility === INVISIBLE) showToolbar()
            false
        } else if (event.action === MotionEvent.ACTION_UP) {
            handler.postDelayed(barHider, HIDE_DELAY)
            false
        }
        super.onTouchEvent(event)

    }
}