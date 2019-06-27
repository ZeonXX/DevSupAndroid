package com.sup.dev.android.views.views

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup


open class ViewTextRounded constructor(context: Context, attrs: AttributeSet) : androidx.appcompat.widget.AppCompatTextView(context, attrs) {

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if(layoutParams.width != ViewGroup.LayoutParams.MATCH_PARENT && gravity != Gravity.CENTER) {
            val width = Math.ceil(getMaxLineWidth().toDouble()).toInt()
            val height = measuredHeight
            setMeasuredDimension(width, height)
        }
    }

    public fun getMaxLineWidth(): Float {
        if(layout == null) return 0f
        var maximumWidth = 0.0f
        val lines = layout.lineCount
        for (i in 0 until lines) {
            maximumWidth = Math.max(layout.getLineWidth(i), maximumWidth)
        }

        return maximumWidth
    }
}