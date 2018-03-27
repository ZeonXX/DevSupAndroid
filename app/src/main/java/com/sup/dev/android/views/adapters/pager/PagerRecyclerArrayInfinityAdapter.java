package com.sup.dev.android.views.adapters.pager;


import android.view.View;

public abstract class PagerRecyclerArrayInfinityAdapter<K, V extends View> extends PagerRecyclerArrayAdapter<K, V> {

    public static int LOOPS_COUNT = 100000;

    public PagerRecyclerArrayInfinityAdapter(int layoutRes) {
        super(layoutRes);
    }

    @Override
    protected int realPosition(int position) {
        return position % getRealCount();
    }

    @Override
    public int getCount() {
        int realCount = getRealCount();
        return realCount == 1 ? 1 : realCount * LOOPS_COUNT;
    }

    public int getRealCount() {
        return super.getCount();
    }

}
