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
    private var key: Any? = null

    init {
        setWillNotDraw(false)
    }

    fun clear() {
        if (inProgress) {
            clear = true
            invalidate()
        } else {
            clearNow()
        }
    }

    private fun clearNow() {
        clear = false
        key = null
        animations.clear()
        addList.clear()
        removeList.clear()
    }

    fun setKey(key: Any?){
        this.key = key
        clear()
    }

    fun addAnimation(animation: DrawAnimation) {
        addAnimation(null, animation)
    }

    fun addAnimation(key: Any?, animation: DrawAnimation) {
        if (this.key != null && this.key !== key) return
        animation.start()
        addList.add(animation)
        animations.add(animation)
        invalidate()
    }

    public override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        inProgress = true
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
            clearNow()
            removed = true
        }
        if (removed || animations.isNotEmpty()) invalidate()
        inProgress = false
    }


}
