package com.sup.dev.android.views.views

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.cards.Card
import com.sup.dev.android.views.support.adapters.pager.PagerCardAdapter
import java.util.*
import kotlin.collections.HashMap

class ViewCalendar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    companion object {
        val TAG_TITLE = "TAG_TITLE"
        val TAG_DAYS_TITLES_CONTAINER = "TAG_DAYS_TITLES_CONTAINER"
        val TAG_DATE_TEXT_ = "TAG_DATE_TEXT_"
        val TAG_DATE_ICON_ = "TAG_DATE_ICON_"
        val TAG_DATES_LINE_ = "TAG_DATES_LINE_"
    }

    private var onPageChanged:()->Unit = {}
    val vIconBack: ViewIcon = ToolsView.inflate(R.layout.z_icon)
    val vIconForward: ViewIcon = ToolsView.inflate(R.layout.z_icon)
    val vPager: ViewPager = ViewPager(context)

    var marks = HashMap<Long, Int>()
    val adapter = PagerCardAdapter()

    init {
        adapter.setCardH(ViewGroup.LayoutParams.WRAP_CONTENT)
        val date = GregorianCalendar.getInstance()
        date.set(GregorianCalendar.DAY_OF_MONTH, 1)
        while (date.get(GregorianCalendar.YEAR) > 2000) {
            while (true) {
                adapter.add(0, PagerCard(date.timeInMillis))

                if (date.get(GregorianCalendar.MONTH) == 0) break
                date.set(GregorianCalendar.MONTH, date.get(GregorianCalendar.MONTH) - 1)
            }
            date.set(GregorianCalendar.YEAR, date.get(GregorianCalendar.YEAR) - 1)
            date.set(GregorianCalendar.MONTH, 11)
        }

        setPadding(ToolsView.dpToPx(16).toInt(), ToolsView.dpToPx(16).toInt(), ToolsView.dpToPx(16).toInt(), ToolsView.dpToPx(16).toInt())
        addView(vPager)
        addView(vIconBack)
        addView(vIconForward)

        (vIconBack.layoutParams as LayoutParams).gravity = Gravity.LEFT or Gravity.TOP
        (vIconForward.layoutParams as LayoutParams).gravity = Gravity.RIGHT or Gravity.TOP

        (vIconBack.layoutParams as LayoutParams).width = ToolsView.dpToPx(48).toInt()
        (vIconBack.layoutParams as LayoutParams).height = ToolsView.dpToPx(48).toInt()
        (vIconForward.layoutParams as LayoutParams).width = ToolsView.dpToPx(48).toInt()
        (vIconForward.layoutParams as LayoutParams).height = ToolsView.dpToPx(48).toInt()
        (vPager.layoutParams as LayoutParams).width = ViewGroup.LayoutParams.MATCH_PARENT
        (vPager.layoutParams as LayoutParams).height = ToolsView.dpToPx(362).toInt()
        vIconBack.setImageResource(R.drawable.ic_keyboard_arrow_left_white_24dp)
        vIconForward.setImageResource(R.drawable.ic_keyboard_arrow_right_white_24dp)

        vPager.adapter = adapter
        vPager.currentItem = adapter.size() - 1
        vIconForward.alpha = 0f
        vPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {

            override fun onPageSelected(position: Int) {
                vIconForward.alpha = if (position == adapter.size() - 1) 0f else 1f
                vIconBack.alpha = if (position == 0) 0f else 1f
                onPageChanged.invoke()
            }
        })
        vIconForward.setOnClickListener {
            if (vPager.currentItem < adapter.size() - 1) vPager.setCurrentItem(vPager.currentItem + 1, true)
        }
        vIconBack.setOnClickListener {
            if (vPager.currentItem > 0) vPager.setCurrentItem(vPager.currentItem - 1, true)
        }
    }

    fun setOnChanged(onPageChanged:()->Unit){
        this.onPageChanged = onPageChanged
    }

    fun clearDateMarks(){
        marks.clear()
        adapter.updateAll()
    }

    fun clearDateMark(dateTime: Long){
        val date = GregorianCalendar.getInstance()
        date.timeInMillis = dateTime
        date.set(GregorianCalendar.HOUR_OF_DAY, 0)
        date.set(GregorianCalendar.MINUTE, 0)
        date.set(GregorianCalendar.SECOND, 0)
        date.set(GregorianCalendar.MILLISECOND, 0)
        marks.remove(date.timeInMillis)
        adapter.updateAll()
    }


    fun setDateMark(dateTime: Long, color: Int?) {
        val date = GregorianCalendar.getInstance()
        date.timeInMillis = dateTime
        date.set(GregorianCalendar.HOUR_OF_DAY, 0)
        date.set(GregorianCalendar.MINUTE, 0)
        date.set(GregorianCalendar.SECOND, 0)
        date.set(GregorianCalendar.MILLISECOND, 0)
        if (color == null) marks.remove(date.timeInMillis)
        else marks.set(date.timeInMillis, color)
        adapter.updateAll()
    }

    private fun getCurrentFrame() = adapter.get(vPager.currentItem) as PagerCard

    fun getStartDate() = getCurrentFrame().getStartDate()

    fun getEndDate() = getCurrentFrame().getEndDate()

    inner class PagerCard(
            val dateTimeMillis: Long
    ) : Card(0) {

        fun getStartDate(): Long {
            val date = GregorianCalendar.getInstance()
            date.timeInMillis = dateTimeMillis
            date.set(GregorianCalendar.DAY_OF_MONTH, 1)
            date.set(GregorianCalendar.HOUR_OF_DAY, 0)
            date.set(GregorianCalendar.MINUTE, 0)
            date.set(GregorianCalendar.SECOND, 0)
            date.set(GregorianCalendar.MILLISECOND, 0)
            return date.timeInMillis
        }

        fun getEndDate(): Long {
            val date = GregorianCalendar.getInstance()
            date.timeInMillis = dateTimeMillis
            date.set(GregorianCalendar.DAY_OF_MONTH, date.getActualMaximum(Calendar.DAY_OF_MONTH))
            date.set(GregorianCalendar.HOUR_OF_DAY, 0)
            date.set(GregorianCalendar.MINUTE, 0)
            date.set(GregorianCalendar.SECOND, 0)
            date.set(GregorianCalendar.MILLISECOND, 0)
            return date.timeInMillis
        }

        override fun bindView(view: View) {
            super.bindView(view)
            val vLinear: LinearLayout = view as LinearLayout
            val vTitle: TextView = view.findViewWithTag(TAG_TITLE)
            val vDaysTitlesContainer: LinearLayout = view.findViewWithTag(TAG_DAYS_TITLES_CONTAINER)

            vLinear.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            vLinear.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            (vLinear.layoutParams as FrameLayout.LayoutParams).gravity = Gravity.TOP

            val date = GregorianCalendar.getInstance()
            date.timeInMillis = dateTimeMillis
            date.set(GregorianCalendar.DAY_OF_MONTH, 1)
            date.set(GregorianCalendar.HOUR_OF_DAY, 0)
            date.set(GregorianCalendar.MINUTE, 0)
            date.set(GregorianCalendar.SECOND, 0)
            date.set(GregorianCalendar.MILLISECOND, 0)
            vTitle.text = date.getDisplayName(GregorianCalendar.MONTH, Calendar.LONG_STANDALONE, Locale.getDefault()).capitalize() + " " + date.get(GregorianCalendar.YEAR)

            var startDay = (date.get(GregorianCalendar.DAY_OF_WEEK) + 5) % 7
            var lastDay = 0
            for (weekIndex in 0 until 6) {
                val vWeekLine: ViewGroup = view.findViewWithTag(TAG_DATES_LINE_ + weekIndex)
                for (dayIndex in 0 until 7) {
                    val vText: TextView = vWeekLine.findViewWithTag(TAG_DATE_TEXT_ + dayIndex)
                    val vIcon: ViewIcon = vWeekLine.findViewWithTag(TAG_DATE_ICON_ + dayIndex)

                    val day = date.get(GregorianCalendar.DAY_OF_MONTH)
                    if (dayIndex < startDay || day < lastDay) {
                        vText.text = ""
                        vIcon.setOnClickListener(null)
                    } else {
                        lastDay = day
                        startDay = 0
                        vText.text = "$day"
                        vIcon.setOnClickListener {
                        }
                        vIcon.setIconBackgroundColor(marks[date.timeInMillis] ?: 0)
                        date.set(GregorianCalendar.DAY_OF_MONTH, day + 1)
                    }

                }
            }

        }

        override fun instanceView(): View {
            val vLinear = LinearLayout(context)
            val vTitle: TextView = ToolsView.inflate(R.layout.z_text_body)
            val vDaysTitlesContainer = LinearLayout(context)

            vLinear.addView(vTitle)
            vLinear.addView(vDaysTitlesContainer)

            vLinear.orientation = LinearLayout.VERTICAL
            vDaysTitlesContainer.orientation = LinearLayout.HORIZONTAL
            vTitle.tag = TAG_TITLE
            vDaysTitlesContainer.tag = TAG_DAYS_TITLES_CONTAINER

            (vTitle.layoutParams as LinearLayout.LayoutParams).gravity = Gravity.CENTER
            (vTitle.layoutParams as LinearLayout.LayoutParams).width = ViewGroup.LayoutParams.WRAP_CONTENT
            (vTitle.layoutParams as LinearLayout.LayoutParams).height = ViewGroup.LayoutParams.WRAP_CONTENT
            (vTitle.layoutParams as LinearLayout.LayoutParams).topMargin = ToolsView.dpToPx(14).toInt()
            (vDaysTitlesContainer.layoutParams as LinearLayout.LayoutParams).width = ViewGroup.LayoutParams.MATCH_PARENT
            (vDaysTitlesContainer.layoutParams as LinearLayout.LayoutParams).height = ViewGroup.LayoutParams.WRAP_CONTENT
            (vDaysTitlesContainer.layoutParams as LinearLayout.LayoutParams).topMargin = ToolsView.dpToPx(24).toInt()

            val daysCalendar = GregorianCalendar.getInstance()
            for (i in 2 until 9) {
                daysCalendar.set(GregorianCalendar.DAY_OF_WEEK, i % 7)
                val vText: TextView = ToolsView.inflate(R.layout.z_text_caption)
                val vFrame = FrameLayout(context)
                vText.text = daysCalendar.getDisplayName(GregorianCalendar.DAY_OF_WEEK, Calendar.SHORT_STANDALONE, Locale.getDefault())
                vFrame.addView(vText)
                vDaysTitlesContainer.addView(vFrame)
                (vText.layoutParams as LayoutParams).width = ViewGroup.LayoutParams.WRAP_CONTENT
                (vText.layoutParams as LayoutParams).height = ViewGroup.LayoutParams.WRAP_CONTENT
                (vText.layoutParams as LayoutParams).gravity = Gravity.CENTER
                (vFrame.layoutParams as LinearLayout.LayoutParams).width = ViewGroup.LayoutParams.WRAP_CONTENT
                (vFrame.layoutParams as LinearLayout.LayoutParams).height = ViewGroup.LayoutParams.WRAP_CONTENT
                (vFrame.layoutParams as LinearLayout.LayoutParams).weight = 1f
            }

            for (i in 0 until 6) {
                val vLine = LinearLayout(context)
                vLine.orientation = LinearLayout.HORIZONTAL
                vLine.tag = TAG_DATES_LINE_ + i
                vLinear.addView(vLine)
                (vLine.layoutParams as LinearLayout.LayoutParams).width = ViewGroup.LayoutParams.MATCH_PARENT
                (vLine.layoutParams as LinearLayout.LayoutParams).height = ViewGroup.LayoutParams.WRAP_CONTENT
                for (ii in 0 until 7) {
                    val vIcon: ViewIcon = ToolsView.inflate(R.layout.z_icon_18)
                    val vText: TextView = ToolsView.inflate(R.layout.z_text_body)
                    val vFrame = FrameLayout(context)
                    vText.text = "-"
                    vText.textSize = 12f
                    vText.gravity = Gravity.CENTER
                    vText.tag = TAG_DATE_TEXT_ + ii
                    vIcon.tag = TAG_DATE_ICON_ + ii
                    vFrame.addView(vIcon)
                    vFrame.addView(vText)
                    vLine.addView(vFrame)
                    (vText.layoutParams as LayoutParams).width = ToolsView.dpToPx(48).toInt()
                    (vText.layoutParams as LayoutParams).height = ToolsView.dpToPx(48).toInt()
                    (vIcon.layoutParams as LayoutParams).width = ToolsView.dpToPx(32).toInt()
                    (vIcon.layoutParams as LayoutParams).height = ToolsView.dpToPx(32).toInt()
                    (vText.layoutParams as LayoutParams).gravity = Gravity.CENTER
                    (vIcon.layoutParams as LayoutParams).gravity = Gravity.CENTER
                    (vFrame.layoutParams as LinearLayout.LayoutParams).width = ViewGroup.LayoutParams.WRAP_CONTENT
                    (vFrame.layoutParams as LinearLayout.LayoutParams).height = ViewGroup.LayoutParams.WRAP_CONTENT
                    (vFrame.layoutParams as LinearLayout.LayoutParams).weight = 1f
                }
            }

            return vLinear
        }

    }

}