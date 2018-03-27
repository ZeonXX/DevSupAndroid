package com.sup.dev.android.views.widgets.pager;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.CallSuper;
import android.support.annotation.MainThread;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsResources;
import com.sup.dev.android.utils.interfaces.UtilsView;
import com.sup.dev.java.libs.debug.Debug;

public abstract class ViewPagerIndicator extends ViewGroup implements ViewPager.OnPageChangeListener {

    private final UtilsView utilsView;
    private final UtilsResources utilsResources;
    protected final Paint paint;

    private final int pagerId;
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
        utilsView = SupAndroid.di.utilsView();
        utilsResources = SupAndroid.di.utilsResources();

        color = utilsResources.getAccentAlphaColor(context);
        colorSelected = utilsResources.getAccentColor(context);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator, 0, 0);
        color = a.getColor(R.styleable.ViewPagerIndicator_ViewPagerIndicator_color, color);
        colorSelected = a.getColor(R.styleable.ViewPagerIndicator_ViewPagerIndicator_colorSelected, colorSelected);
        offset = a.getDimension(R.styleable.ViewPagerIndicator_ViewPagerIndicator_offset, utilsView.dpToPx(16));
        pagerId = a.getResourceId(R.styleable.ViewPagerIndicator_ViewPagerIndicator_pager, 0);
        a.recycle();

        paint = new Paint();
        paint.setAntiAlias(true);

    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(pager == null && pagerId != 0) setPager(utilsView.findViewOnParents(this, pagerId));
        super.onDraw(canvas);
    }

    @MainThread
   public void setPager(ViewPager pager) {
       this.pager = pager;
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

}
