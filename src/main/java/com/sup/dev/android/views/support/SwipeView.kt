package com.sup.dev.android.views.support

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.java.classes.Subscription
import com.sup.dev.java.tools.ToolsColor
import com.sup.dev.java.tools.ToolsMath
import com.sup.dev.java.tools.ToolsThreads

class SwipeView(private val view: ViewGroup,
                private val vIconForAlphaAnimation: View?,
                private val colorDefault: Int,
                private val onClick: (Float, Float) -> Unit = { x, y -> },
                private val onLongClick: (Float, Float) -> Unit = { x, y -> },
                private val onSwipe: () -> Unit = {}
) : View.OnTouchListener {

    private val maxOffset = ToolsView.dpToPx(48)
    private val longClickTime: Long = 200

    private val colorFocus = ToolsResources.getColor(R.color.focus)
    private val colorFocusAlpha = ToolsColor.alpha(colorFocus).toFloat()
    private val alphaAnimationTime = 500f
    private val alphaAnimationStep = 10f

    private var swipeEnabled = true
    private var swiped = false
    private var swipeStarted = false
    private var startX = -1f
    private var firstX = -1f
    private var firstY = -1f
    private var firstClickTime = -1L
    private var lastX = -1f
    private var lastY = -1f
    private var focusAlpha = 0f

    private var subscriptionBack: Subscription? = null
    private var subscriptionFocus: Subscription? = null
    private var subscriptionLongClick: Subscription? = null

    init {
        view.setOnTouchListener(this)
        vIconForAlphaAnimation?.alpha = 0f
        view.setTag(this)
    }

    fun setSwipeEnabled(enabled: Boolean) {
        swipeEnabled = enabled
    }

    override fun onTouch(view: View, e: MotionEvent): Boolean {
        if (view.tag != this) return false
        if (subscriptionBack != null) subscriptionBack!!.unsubscribe()
        if (subscriptionLongClick != null) subscriptionLongClick!!.unsubscribe()

        if (e.action == MotionEvent.ACTION_DOWN) {
            startX = view.x
            lastX = e.x
            lastY = e.y
            firstX = e.x
            firstY = e.y
            firstClickTime = System.currentTimeMillis()

            subscriptionFocus = ToolsThreads.timerMain(alphaAnimationStep.toLong(), alphaAnimationTime.toLong(), { sub ->
                focusAlpha += colorFocusAlpha / alphaAnimationTime * alphaAnimationStep
                focusAlpha = ToolsMath.min(focusAlpha, colorFocusAlpha)
                view.setBackgroundColor(ToolsColor.add(colorDefault, ToolsColor.setAlpha(focusAlpha.toInt(), colorFocus)))
                if (view.tag != this) sub.unsubscribe()
            }, {
                focusAlpha = colorFocusAlpha
                view.setBackgroundColor(ToolsColor.add(colorDefault, colorFocus))
            })

            subscriptionLongClick = ToolsThreads.main(longClickTime) {
                clear()
                onLongClick.invoke(e.x, e.y)
            }
            return true
        }
        if (e.action == MotionEvent.ACTION_MOVE && lastX > -1 && swipeEnabled) {
            val mx = e.x - (startX - view.x)

            if (!swipeStarted && (Math.abs(firstX - mx) / 6 <= Math.abs(firstY - e.y) || firstX - mx < ToolsView.dpToPx(24))) {
                if (firstClickTime < System.currentTimeMillis() - longClickTime) {
                    clear()
                    onLongClick.invoke(e.x, e.y)
                    return true
                }
                lastX = e.x
                lastY = e.y
                return false
            } else {
                this.view.requestDisallowInterceptTouchEvent(true)
                swipeStarted = true
            }

            view.x = view.x - (lastX - mx)
            if (view.x < startX - maxOffset) {
                view.x = startX - maxOffset
                swiped = true
            } else {
                swiped = false
            }
            if (view.x > startX) {
                view.x = startX
            }
            vIconForAlphaAnimation?.alpha = (startX - view.x) / maxOffset
            lastX = mx
            lastY = e.y
            return true
        }
        if (e.action == MotionEvent.ACTION_UP && firstX == e.x && firstY == e.y) {
            if (firstClickTime < System.currentTimeMillis() - longClickTime)
                onLongClick.invoke(e.x, e.y)
            else
                onClick.invoke(e.x, e.y)
            clear()
            return true
        }
        if (e.action == MotionEvent.ACTION_CANCEL || e.action == MotionEvent.ACTION_UP) {
            clear()
            return true
        }
        return false
    }

    private fun clear() {
        startX = -1f
        lastX = -1f
        lastY = -1f
        firstX = -1f
        firstY = -1f
        firstClickTime = -1
        swipeStarted = false
        if (subscriptionBack != null) subscriptionBack!!.unsubscribe()
        if (subscriptionFocus != null) subscriptionFocus!!.unsubscribe()
        if (subscriptionLongClick != null) subscriptionLongClick!!.unsubscribe()

        val stepTime = 10L
        val animationTime = 150L
        val step = -view.x / (animationTime / stepTime.toFloat())
        subscriptionBack = ToolsThreads.timerMain(stepTime, animationTime, { sub ->
            view.x += step
            if (view.x > 0) view.x = 0f
            vIconForAlphaAnimation?.alpha = (startX - view.x) / maxOffset
        }, {
            view.x = 0f
            vIconForAlphaAnimation?.alpha = 0f
            if (swiped) {
                onSwipe.invoke()
                swiped = false
            }
        })
        subscriptionFocus = ToolsThreads.timerMain(alphaAnimationStep.toLong(), alphaAnimationTime.toLong(), { sub ->
            focusAlpha -= colorFocusAlpha / alphaAnimationTime * alphaAnimationStep
            focusAlpha = ToolsMath.max(focusAlpha, 0f)
            view.setBackgroundColor(ToolsColor.add(colorDefault, ToolsColor.setAlpha(focusAlpha.toInt(), colorFocus)))
            if (view.tag != this) sub.unsubscribe()
        }, {
            focusAlpha = 0f
            view.setBackgroundColor(colorDefault)
        })
    }


}