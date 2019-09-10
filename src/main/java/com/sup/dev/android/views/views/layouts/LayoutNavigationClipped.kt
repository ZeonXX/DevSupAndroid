package com.sup.dev.android.views.views.layouts

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsToast
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.views.ViewIcon

class LayoutNavigationClipped @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val path = Path()
    private var paint: Paint? = null
    private var cornedSize = ToolsView.dpToPx(16)
    private var cornedSides = true
    private val vIconMain: ViewIcon = ToolsView.inflate(R.layout.z_icon)
    private val vIcon1: ViewIcon = ToolsView.inflate(R.layout.z_icon)
    private val vIcon2: ViewIcon = ToolsView.inflate(R.layout.z_icon)
    private val vIcon3: ViewIcon = ToolsView.inflate(R.layout.z_icon)
    private val vIcon4: ViewIcon = ToolsView.inflate(R.layout.z_icon)

    init {
        setWillNotDraw(false)
        setBackgroundColor(ToolsResources.getColorAttr(R.attr.colorPrimary))


        initIcon(vIconMain)
        initIcon(vIcon1)
        initIcon(vIcon2)
        initIcon(vIcon3)
        initIcon(vIcon4)

        vIconMain.setIconBackgroundColor(ToolsResources.getColorAttr(R.attr.colorPrimary))
        vIconMain.setPadding(ToolsView.dpToPx(16).toInt(), ToolsView.dpToPx(16).toInt(), ToolsView.dpToPx(16).toInt(), ToolsView.dpToPx(16).toInt())
        vIconMain.layoutParams.width = ToolsView.dpToPx(56).toInt()
        vIconMain.layoutParams.height = ToolsView.dpToPx(56).toInt()
    }

    private fun initIcon(vIcon:ViewIcon){
        addView(vIcon)
        vIcon.setImageResource(R.drawable.ic_menu_white_24dp)
        vIcon.layoutParams.width = ToolsView.dpToPx(48).toInt()
        vIcon.layoutParams.height = ToolsView.dpToPx(48).toInt()
        vIcon.setOnClickListener {
            ToolsToast.show("Click")
        }
    }

    override fun draw(canvas: Canvas?) {
        if (paint != null && paint!!.color != 0) canvas?.drawPath(path, paint!!)
        super.draw(canvas)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        update()
    }

    private fun update() {
        path.reset()

        val buttonSize = vIconMain.width/2
        val buttonOffset = ToolsView.dpToPx(6)

        val arg3 = buttonSize + buttonOffset
        val r = Math.min(Math.min(cornedSize, width.toFloat() / 2), height.toFloat() / 2)
        val r2 = ToolsView.dpToPx(8)


        path.addRect(r, arg3, width - r, r + arg3, Path.Direction.CCW)
        path.addRect(0f, height.toFloat(), width.toFloat(), height - r, Path.Direction.CCW)
        path.addRect(0f, r + arg3, width.toFloat(), height - r, Path.Direction.CCW)

        val pBigCircle = Path()
        pBigCircle.addArc(width / 2f - arg3, r2 / 2, width / 2f + arg3, arg3 * 2 + r2 / 2, 0f, 180f)
        pBigCircle.addRect(width / 2f - arg3 - r2, arg3, width / 2f + arg3 + r2, arg3 + r2, Path.Direction.CW)
        path.op(pBigCircle, Path.Op.XOR)

        val pCircles = Path()
        pCircles.addCircle(width / 2f - arg3 - r2, arg3 + r2, r2, Path.Direction.CW)
        pCircles.addCircle(width / 2f + arg3 + r2, arg3 + r2, r2, Path.Direction.CW)
        if (cornedSides) pCircles.addCircle(r, r + arg3, r, Path.Direction.CCW); else pCircles.addRect(0f, arg3, r, r + arg3, Path.Direction.CCW)
        if (cornedSides) pCircles.addCircle(width - r, r + arg3, r, Path.Direction.CCW); else pCircles.addRect(width - r, arg3, width.toFloat(), r + arg3, Path.Direction.CCW)
        path.op(pCircles, Path.Op.UNION)

        vIconMain.x = width / 2f - vIconMain.width / 2
        vIconMain.y = arg3 - vIconMain.width / 2

        vIcon1.x = (width/2 - arg3) / 4 - vIcon1.width/2
        vIcon2.x = (width/2 - arg3) / 4 * 3 - vIcon2.width/2
        vIcon3.x = (width - (width/2 - arg3) / 4) - vIcon1.width/2
        vIcon4.x = (width - (width/2 - arg3) / 4 * 3) - vIcon2.width/2

        vIcon1.y = arg3
        vIcon2.y = arg3
        vIcon3.y = arg3
        vIcon4.y = arg3

        invalidate()
    }

    fun setBackgroundRes(r: Int) {
        setBackgroundColor(ToolsResources.getColor(r))
    }

    override fun setBackground(background: Drawable?) {

        if (paint == null) {
            paint = Paint()
            paint!!.isAntiAlias = true
            paint!!.color = 0
        }

        if (background != null && background is ColorDrawable) {
            paint!!.color = background.color
            super.setBackground(null)
        } else {
            paint!!.color = 0
            super.setBackground(background)
        }
    }


}