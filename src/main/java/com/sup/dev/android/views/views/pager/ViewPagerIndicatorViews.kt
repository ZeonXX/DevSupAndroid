package com.sup.dev.android.views.views.pager

import android.content.Context
import androidx.annotation.MainThread
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet
import android.view.View
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsView
import java.lang.RuntimeException

open class ViewPagerIndicatorViews @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPagerIndicator(context, attrs), androidx.viewpager.widget.ViewPager.OnPageChangeListener {

    var viewsProvider:(Int)->View = {throw RuntimeException("You must set provider")}
    var offsetLeft = 0
    var views: Array<View> = emptyArray()

    init {

        SupAndroid.initEditMode(this)

        val a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicatorViews, 0, 0)
        offsetLeft = a.getDimension(R.styleable.ViewPagerIndicatorViews_ViewPagerIndicatorViews_offset_left, offsetLeft.toFloat()).toInt()
        a.recycle()
    }

    @MainThread
    override fun onChanged() {
        requestLayout()
    }

    override fun onAdapterChanged(viewPager: androidx.viewpager.widget.ViewPager, oldAdapter: androidx.viewpager.widget.PagerAdapter?, newAdapter: androidx.viewpager.widget.PagerAdapter?) {
        reset()
    }

    override fun setPagerView(pager: androidx.viewpager.widget.ViewPager?) {
        super.setPagerView(pager)
        reset()
    }

    fun reset(){
        removeAllViews()
        if (pager!!.adapter == null) return

        views = Array(pager!!.adapter!!.count){
            val v = instanceView(it)
            v.setOnClickListener { vc -> pager!!.currentItem = it }
            addView(v)
            v
        }
    }

    open fun instanceView(index:Int) = viewsProvider.invoke(index)

    //
    //  Layout
    //

    @MainThread
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (pager == null && pagerId != 0) setPagerView(ToolsView.findViewOnParents(this, pagerId))

        for (i in 0 until childCount) {
            val v = getChildAt(i)
            measureChild(v, widthMeasureSpec, heightMeasureSpec)
        }
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(ToolsView.dpToPx(48).toInt(), MeasureSpec.EXACTLY))
    }

    @MainThread
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        val count = childCount
        if (count == 0) return
        var xOffset = 0
        for (i in 0 until position) {
            val child = getChildAt(i)
            xOffset += (child.measuredWidth + offset).toInt()
        }

        val selected = views[position]
        val oldIndex = if (positionOffset > 0) position + 1 else position - 1
        if (positionOffset != 0f && oldIndex > -1 && oldIndex < count) {
            val old = views[oldIndex]
            xOffset += (((selected.measuredWidth + old.measuredWidth) / 2 + offset) * positionOffset).toInt()
        }


        var x = (width - offsetLeft - selected.measuredWidth) / 2 - xOffset
        for (i in 0 until count) {
            val child = getChildAt(i)
            val y = (height - child.measuredHeight) / 2
            child.layout(x, y, x + child.measuredWidth, child.measuredHeight + y)
            x += (offset + child.measuredWidth).toInt()
        }

    }


}