package com.sup.dev.android.libs.visual_engine

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.sup.dev.java.libs.debug.log
import com.sup.dev.java.libs.visual_engine.graphics.VeGraphics
import com.sup.dev.java.libs.visual_engine.root.VeGui

class VeGraphicsAndroid(
) : VeGraphics() {

    private val paint = Paint()
    private var canvas = Canvas()

    init {
        paint.isAntiAlias = true
    }

    fun setCanvas(canvas: Canvas) {
        this.canvas = canvas
        clearOffset()
    }

    override fun drawString(string: String, x: Float, y: Float) {
        paint.color = getColor()
        paint.typeface = VeGuiAndroid.getTypeface(getFont())
        paint.textSize = getFont().size
        canvas.drawText(string, getOffsetX() + x, getOffsetY() + y + VeGui.getFontSize(getFont()), paint)
    }

    override fun drawLine(x1: Float, y1: Float, x2: Float, y2: Float) {
        paint.color = getColor()
        paint.typeface = VeGuiAndroid.getTypeface(getFont())
        paint.textSize = getFont().size
        canvas.drawLine(getOffsetX() + x1, getOffsetY() + y1, getOffsetX() + x2, getOffsetY() + y2, paint)
    }

    override fun fillRect(x: Float, y: Float, w: Float, h: Float) {
        paint.color = getColor()
        paint.strokeWidth = getStrokeSize()
        paint.style = Paint.Style.FILL
        canvas.drawRect(getOffsetX() + x, getOffsetY() + y, getOffsetX() + x + w, getOffsetY() + y + h, paint)
    }

    override fun drawRect(x: Float, y: Float, w: Float, h: Float) {
        paint.color = getColor()
        paint.strokeWidth = getStrokeSize()
        paint.style = Paint.Style.STROKE
        canvas.drawRect(getOffsetX() + x, getOffsetY() + y, getOffsetX() + x + w, getOffsetY() + y + h, paint)
    }

    override fun fillCircle(x: Float, y: Float, w: Float, h: Float) {
        paint.color = getColor()
        paint.strokeWidth = getStrokeSize()
        paint.style = Paint.Style.FILL
        canvas.drawArc(RectF(getOffsetX() + x, getOffsetY() + y, getOffsetX() + x + w, getOffsetY() + y + h), 0f, 360f, true, paint)
    }

    override fun fillCircle(cx: Float, cy: Float, r: Float) {
        paint.color = getColor()
        paint.strokeWidth = getStrokeSize()
        paint.style = Paint.Style.FILL
        canvas.drawCircle(getOffsetX() + cx, getOffsetY() + cy, r, paint)
    }

    override fun drawCircle(x: Float, y: Float, w: Float, h: Float) {
        paint.color = getColor()
        paint.strokeWidth = getStrokeSize()
        paint.style = Paint.Style.STROKE
        canvas.drawArc(RectF(getOffsetX() + x, getOffsetY() + y, getOffsetX() + x + w, getOffsetY() + y + h), 0f, 360f, true, paint)
    }

    override fun drawCircle(cx: Float, cy: Float, r: Float) {
        paint.color = getColor()
        paint.strokeWidth = getStrokeSize()
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(getOffsetX() + cx, getOffsetY() + cy, r, paint)
    }


}