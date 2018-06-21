package com.sup.dev.android.views.adapters.pager;


public abstract class PagerCardInfinityAdapter extends PagerCardAdapter {

    public static int LOOPS_COUNT = 100000;

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
