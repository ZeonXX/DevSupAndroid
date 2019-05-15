package com.sup.dev.android.views.views.table

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.java.tools.ToolsColor
import com.sup.dev.java.tools.ToolsMath
import java.util.ArrayList

class ViewTable @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    private var columnsCount = 0
    private var minCellW = ToolsView.dpToPx(56)
    private var minCellH = ToolsView.dpToPx(56)
    internal var onCellClicked: (ViewTableCell, Int, Int) -> Unit = { v, x, y -> }

    init {
        setBackgroundColor(ToolsResources.getColor(R.color.focus_dark))
        orientation = VERTICAL
        setPadding(ToolsView.dpToPx(1).toInt(), ToolsView.dpToPx(1).toInt(), 0, 0)
    }

    fun removeRow(index: Int) {
        removeViewAt(index)
    }

    fun createRow(bottom: Boolean) {
        val row = ViewTableRow(this)
        if (bottom) addView(row)
        else addView(row, 0)
        row.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
    }

    fun createRows(count: Int, right: Boolean) {
        for (i in 0 until count) createRow(right)
    }

    private fun getRow(index: Int) = getChildAt(index) as ViewTableRow


    //
    //  Setters
    //

    fun setColumnsCount(columnsCount: Int, right: Boolean) {
        this.columnsCount = columnsCount
        for (i in 0 until getRowsCount()) getRow(i).resetCells(right)
    }

    fun setMinCellW(minCellW: Float) {
        this.minCellW = minCellW
        for (i in 0 until getRowsCount()) getRow(i).resetCellMinSizes()
    }

    fun setMinCellH(minCellH: Float) {
        this.minCellH = minCellH
        for (i in 0 until getRowsCount()) getRow(i).resetCellMinSizes()
    }

    fun setOnCellClicked(onCellClicked: (ViewTableCell, Int, Int) -> Unit) {
        this.onCellClicked = onCellClicked
    }

    //
    //  Getters
    //

    fun getRowsCount() = childCount

    fun getColumnsCount() = columnsCount

    fun getMinCellW() = minCellW

    fun getMinCellH() = minCellH

}
