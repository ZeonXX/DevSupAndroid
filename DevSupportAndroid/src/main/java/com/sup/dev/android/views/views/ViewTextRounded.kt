package com.sup.dev.android.views.views

import android.content.Context
import android.text.Layout
import android.util.AttributeSet


open class ViewTextRounded constructor(context: Context, attrs: AttributeSet) : android.support.v7.widget.AppCompatTextView(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = Math.ceil(getMaxLineWidth(layout).toDouble()).toInt()
        val height = measuredHeight
        setMeasuredDimension(width, height)
    }

    private fun getMaxLineWidth(layout: Layout): Float {
        var maximumWidth = 0.0f
        val lines = layout.lineCount
        for (i in 0 until lines) {
            maximumWidth = Math.max(layout.getLineWidth(i), maximumWidth)
        }

        return maximumWidth
    }
}