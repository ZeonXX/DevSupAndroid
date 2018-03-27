package com.sup.dev.android.views.widgets.pager;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.MainThread;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsView;

public class ViewPagerIndicatorTitles extends ViewPagerIndicator implements View.OnClickListener {

    private final UtilsView utilsView;
    private int resId;
    private int titleId = R.layout.w_indicator_title;
    private int offsetLeft = R.id.title;

    private TextView[] views;


    @MainThread
    public ViewPagerIndicatorTitles(Context context, AttributeSet attrs) {
        super(context, attrs);

        SupAndroid.initEditMode(this);
        utilsView = SupAndroid.di.utilsView();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicatorTitles, 0, 0);
        offsetLeft = (int) a.getDimension(R.styleable.ViewPagerIndicatorTitles_ViewPagerIndicatorTitles_offset_left, offsetLeft);
        resId = a.getResourceId(R.styleable.ViewPagerIndicatorTitles_ViewPagerIndicatorTitles_titleLayout, resId);
        titleId = a.getResourceId(R.styleable.ViewPagerIndicatorTitles_ViewPagerIndicatorTitles_titleId, titleId);
        a.recycle();
    }

    public void reset() {
        //removeAllViews();
        //views = new TextView[pager.getPageCount()];
        //for (int i = 0; i < views.length; i++) {
        //    views[i] = utilsView.inflate(getContext(), resId);
        //    views[i].setText(pager.getPageTitle(i));
        //    views[i].setOnClickListener(this);
        //    addView(views[i]);
        //}
    }

    @Override
    @MainThread
    protected void onChanged() {
        requestLayout();
    }

    @Override
    public void onClick(View v) {
        //  for (int i = 0; i < views.length; i++)
        //      if (v == views[i])
        //          pager.toPage(i);
    }

    @Override
    @MainThread
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            measureChild(v, widthMeasureSpec, heightMeasureSpec);
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
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

        // TextView selected = (views[position].findViewById(titleId));
        // TextView old = null;
        // int oldIndex = (positionOffset > 0) ? position + 1 : position - 1;
        // if (positionOffset != 0 && oldIndex > -1 && oldIndex < count) {
        //     old = (views[oldIndex].findViewById(titleId));
        //     xOffset += ((selected.getMeasuredWidth() + old.getMeasuredWidth()) / 2 + offset) * positionOffset;
        // }

        // int x = (getWidth() - offsetLeft - selected.getMeasuredWidth()) / 2 - xOffset;
        // for (int i = 0; i < count; i++) {
        //     View child = getChildAt(i);
        //     int y = (getHeight() - child.getMeasuredHeight()) / 2;
        //     child.layout(x, y, x + child.getMeasuredWidth(), child.getMeasuredHeight() + y);
        //     x += offset + child.getMeasuredWidth();

        //     TextView textView = (views[i].findViewById(titleId));
        //     textView.setTextColor(ToolsColor.setAlpha(textView.getCurrentTextColor(), 120));
        // }

        //  if (positionOffset != 0) {
        //      float arg = Math.abs(positionOffset);
        //      selected.setTextColor(ToolsColor.setAlpha(selected.getCurrentTextColor(), 120 + (int) ((255 - 120) * (1 - arg))));
        //      old.setTextColor(ToolsColor.setAlpha(old.getCurrentTextColor(), 120 + (int) ((255 - 120) * arg)));
        //  } else {
        //      selected.setTextColor(ToolsColor.setAlpha(selected.getCurrentTextColor(), 255));
        //  }


    }


}
