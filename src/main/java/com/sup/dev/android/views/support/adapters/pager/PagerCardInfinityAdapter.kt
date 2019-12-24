package com.sup.dev.android.views.support.adapters.pager

class PagerCardInfinityAdapter : PagerCardAdapter() {

    companion object {
        var LOOPS_COUNT = 100000
    }

    val realCount: Int
        get() = super.getCount()

    override fun realPosition(position: Int): Int {
        return position % realCount
    }

    override fun getCount(): Int {
        val realCount = realCount
        return if (realCount == 1) 1 else realCount * LOOPS_COUNT
    }

}

