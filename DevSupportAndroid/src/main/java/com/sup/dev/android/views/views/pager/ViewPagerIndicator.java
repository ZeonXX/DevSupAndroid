package com.sup.dev.android.views.views.pager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.sup.dev.android.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;

public abstract class ViewPagerIndicator extends ViewGroup implements ViewPager.OnPageChangeListener, ViewPager.OnAdapterChangeListener {

    protected final Paint paint;

    protected final int pagerId;
    protected int color;
    protected int colorSelected;
    protected float offset;

    protected ViewPager pager;
    protected int position;
    protected float positionOffset;
    protected int state;

    @MainThread
    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        SupAndroid.initEditMode(this);

        offset = ToolsView.dpToPx(16);
        color = ToolsResources.getAccentAlphaColor(context);
        colorSelected = ToolsResources.getAccentColor(context);

       TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator, 0, 0);
       color = a.getColor(R.styleable.ViewPagerIndicator_ViewPagerIndicator_color, color);
       colorSelected = a.getColor(R.styleable.ViewPagerIndicator_ViewPagerIndicator_colorSelected, colorSelected);
       offset = a.getDimension(R.styleable.ViewPagerIndicator_ViewPagerIndicator_offset, offset);
       pagerId = a.getResourceId(R.styleable.ViewPagerIndicator_ViewPagerIndicator_pager, 0);
       a.recycle();

        paint = new Paint();
        paint.setAntiAlias(true);

    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(pager == null && pagerId != 0) setPager(ToolsView.findViewOnParents(this, pagerId));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @MainThread
    public void setPager(ViewPager pager) {
        this.pager = pager;
        pager.addOnPageChangeListener(this);
        pager.addOnAdapterChangeListener(this);
        onChanged();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        this.position = position;
        this.positionOffset = positionOffset;
        onChanged();
    }

    @Override
    public void onPageSelected(int position) {
        if (state == ViewPager.SCROLL_STATE_IDLE)
            this.position = position;
        onChanged();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        this.state = state;
        onChanged();
    }

    protected abstract void onChanged();

    @Override
    public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {

    }
}