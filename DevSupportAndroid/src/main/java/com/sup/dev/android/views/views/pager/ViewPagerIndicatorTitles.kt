package com.sup.dev.android.views.views.pager

import android.content.Context
import android.support.annotation.MainThread
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.java.tools.ToolsColor


class ViewPagerIndicatorTitles @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPagerIndicator(context, attrs) {

    private var offsetLeft: Int = 0

    private var titles: Array<out String?>? = null
    private var views: Array<TextView?>? = null

    init {

        SupAndroid.initEditMode(this)

        val a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicatorTitles, 0, 0)
        offsetLeft = a.getDimension(R.styleable.ViewPagerIndicatorTitles_ViewPagerIndicatorTitles_offset_left, offsetLeft.toFloat()).toInt()
        a.recycle()
    }

    fun setTitles(vararg titles: String) {
        this.titles = titles
    }

    fun setTitles(vararg titles: Int) {
        this.titles = arrayOfNulls(titles.size)
        for (i in titles.indices)
            (this.titles as Array<String?>)[i] = ToolsResources.getString(titles[i])
    }

    override fun onAdapterChanged(viewPager: ViewPager, oldAdapter: PagerAdapter?, newAdapter: PagerAdapter?) {
        reset()
    }

    override fun setPagerView(pager: ViewPager?) {
        super.setPagerView(pager)
        reset()
    }

    fun reset() {
        removeAllViews()
        if (pager!!.adapter == null) return

        views = arrayOfNulls(pager!!.adapter!!.count)
        for (i in views!!.indices) {
            views!![i] = ToolsView.inflate(context, R.layout.view_indicator_title)
            views!![i]!!.text = if (titles!!.size <= i) null else titles!![i]
            views!![i]!!.setOnClickListener { v -> pager!!.currentItem = i }
            addView(views!![i])
        }
    }

    @MainThread
    override fun onChanged() {
        requestLayout()
    }

    fun setOffsetLeft(offsetLeft: Int) {
        this.offsetLeft = offsetLeft
    }

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
        super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(ToolsView.dpToPx(48), View.MeasureSpec.EXACTLY))
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

        val selected = views!![position]!!.findViewById<TextView>(R.id.dev_sup_title)
        var old: TextView? = null
        val oldIndex = if (positionOffset > 0) position + 1 else position - 1
        if (positionOffset != 0f && oldIndex > -1 && oldIndex < count) {
            old = views!![oldIndex]!!.findViewById(R.id.dev_sup_title)
            xOffset += (((selected.measuredWidth + old!!.measuredWidth) / 2 + offset) * positionOffset).toInt()
        }

        var x = (width - offsetLeft - selected.measuredWidth) / 2 - xOffset
        for (i in 0 until count) {
            val child = getChildAt(i)
            val y = (height - child.measuredHeight) / 2
            child.layout(x, y, x + child.measuredWidth, child.measuredHeight + y)
            x += (offset + child.measuredWidth).toInt()

            val textView = views!![i]!!.findViewById<TextView>(R.id.dev_sup_title)
            textView.setTextColor(ToolsColor.setAlpha(120, textView.currentTextColor))
        }

        if (positionOffset != 0f) {
            val arg = Math.abs(positionOffset)
            selected.setTextColor(ToolsColor.setAlpha(120 + ((255 - 120) * (1 - arg)).toInt(), selected.currentTextColor))
            old!!.setTextColor(ToolsColor.setAlpha(120 + ((255 - 120) * arg).toInt(), old.currentTextColor))
        } else {
            selected.setTextColor(ToolsColor.setAlpha(255, selected.currentTextColor))
        }


    }


}