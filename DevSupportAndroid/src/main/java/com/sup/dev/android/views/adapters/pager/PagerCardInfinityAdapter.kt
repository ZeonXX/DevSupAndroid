package com.sup.dev.android.views.adapters.pager

abstract class PagerCardInfinityAdapter : PagerCardAdapter() {

    val realCount: Int
        get() = super.getCount()

    override fun realPosition(position: Int): Int {
        return position % realCount
    }

    override fun getCount(): Int {
        val realCount = realCount
        return if (realCount == 1) 1 else realCount * LOOPS_COUNT
    }

    companion object {

        var LOOPS_COUNT = 100000
    }

}
