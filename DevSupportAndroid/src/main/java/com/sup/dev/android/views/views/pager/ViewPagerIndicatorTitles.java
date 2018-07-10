package com.sup.dev.android.views.views.pager;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.sup.dev.android.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.tools.ToolsColor;

public class ViewPagerIndicatorTitles extends ViewPagerIndicator {

    private int offsetLeft;

    private String[] titles;
    private TextView[] views;

    public ViewPagerIndicatorTitles(Context context) {
        this(context, null);
    }

    public ViewPagerIndicatorTitles(Context context, AttributeSet attrs) {
        super(context, attrs);

        SupAndroid.initEditMode(this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicatorTitles, 0, 0);
        offsetLeft = (int) a.getDimension(R.styleable.ViewPagerIndicatorTitles_ViewPagerIndicatorTitles_offset_left, offsetLeft);
        a.recycle();
    }

    public void setTitles(String... titles) {
        this.titles = titles;
    }

    public void setTitles(int... titles) {
        this.titles = new String[titles.length];
        for (int i = 0; i < titles.length; i++)
            this.titles[i] = ToolsResources.getString(titles[i]);
    }

    @Override
    public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {
        reset();
    }

    @Override
    public void setPager(ViewPager pager) {
        super.setPager(pager);
        reset();
    }

    public void reset() {
        removeAllViews();
        if (pager.getAdapter() == null) return;

        views = new TextView[pager.getAdapter().getCount()];
        for (int i = 0; i < views.length; i++) {
            int index = i;
            views[i] = ToolsView.inflate(getContext(), R.layout.view_indicator_title);
            views[i].setText(titles.length <= i ? null : titles[i]);
            views[i].setOnClickListener(v -> pager.setCurrentItem(index));
            addView(views[i]);
        }
    }

    @Override
    @MainThread
    protected void onChanged() {
        requestLayout();
    }

    public void setOffsetLeft(int offsetLeft) {
        this.offsetLeft = offsetLeft;
    }

    //
    //  Layout
    //

    @Override
    @MainThread
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (pager == null && pagerId != 0) setPager(ToolsView.findViewOnParents(this, pagerId));

        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            measureChild(v, widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(ToolsView.dpToPx(48), MeasureSpec.EXACTLY));
    }

    @Override
    @MainThread
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        final int count = getChildCount();
        if (count == 0) return;
        int xOffset = 0;
        for (int i = 0; i < position; i++) {
            View child = getChildAt(i);
            xOffset += child.getMeasuredWidth() + offset;
        }

        TextView selected = (views[position].findViewById(R.id.dev_sup_title));
        TextView old = null;
        int oldIndex = (positionOffset > 0) ? position + 1 : position - 1;
        if (positionOffset != 0 && oldIndex > -1 && oldIndex < count) {
            old = (views[oldIndex].findViewById(R.id.dev_sup_title));
            xOffset += ((selected.getMeasuredWidth() + old.getMeasuredWidth()) / 2 + offset) * positionOffset;
        }

        int x = (getWidth() - offsetLeft - selected.getMeasuredWidth()) / 2 - xOffset;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            int y = (getHeight() - child.getMeasuredHeight()) / 2;
            child.layout(x, y, x + child.getMeasuredWidth(), child.getMeasuredHeight() + y);
            x += offset + child.getMeasuredWidth();

            TextView textView = (views[i].findViewById(R.id.dev_sup_title));
            textView.setTextColor(ToolsColor.setAlpha(120, textView.getCurrentTextColor()));
        }

        if (positionOffset != 0) {
            float arg = Math.abs(positionOffset);
            selected.setTextColor(ToolsColor.setAlpha(120 + (int) ((255 - 120) * (1 - arg)), selected.getCurrentTextColor()));
            old.setTextColor(ToolsColor.setAlpha(120 + (int) ((255 - 120) * arg), old.getCurrentTextColor()));
        } else {
            selected.setTextColor(ToolsColor.setAlpha(255, selected.getCurrentTextColor()));
        }


    }


}