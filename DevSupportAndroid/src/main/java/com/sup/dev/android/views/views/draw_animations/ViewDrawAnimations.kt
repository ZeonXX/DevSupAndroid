package com.sup.dev.android.views.views.draw_animations

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class ViewDrawAnimations @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val addList = ArrayList<DrawAnimation>()
    private val removeList = ArrayList<DrawAnimation>()
    private val animations = ArrayList<DrawAnimation>()
    private var clear = false
    private var inProgress = false

    init {
        setWillNotDraw(false)
    }

    fun clear() {
        if (inProgress) {
            clear = true
            invalidate()
        } else {
            animations.clear()
            addList.clear()
            removeList.clear()
        }
    }


    fun addAnimation(animation: DrawAnimation) {
        animation.start()
        addList.add(animation)
        animations.add(animation)
        invalidate()
    }

    public override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        inProgress = true;
        for (a in animations) {
            a.update(0.02f)
            a.draw(canvas)
            if (a.needRemove) removeList.add(a)
        }

        var removed = removeList.isNotEmpty()
        for (a in removeList) animations.remove(a)
        removeList.clear()

        for (a in addList) {
            a.start()
            if (!a.needRemove) animations.add(a)
        }
        addList.clear()

        if (clear) {
            clear = false
            animations.clear()
            removed = true
        }
        if (removed || animations.isNotEmpty()) invalidate()
        inProgress = false
    }


}
