package com.sup.dev.android.views.views.pager;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.MainThread;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class ViewPagerIndicatorCircles extends ViewPagerIndicator implements ViewPager.OnPageChangeListener {

    @MainThread
    public ViewPagerIndicatorCircles(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    @Override
    @MainThread
    protected void onChanged() {
        invalidate();
    }

    @Override
    @MainThread
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int count = isInEditMode() ? 3 : (pager != null ? pager.getAdapter().getCount() : 1);
        if (count == 0) count = 1;
        float r = getHeight() / 2;
        float w = offset * (count - 1) + r * 2 * count;
        float x = (getWidth() - w) / 2 + r;
        float a = r * 2 + offset;

        paint.setColor(color);
        for (int i = 0; i < count; i++) {
            canvas.drawCircle(x, r, r, paint);
            x += a;
        }

        paint.setColor(colorSelected);

        x = (getWidth() - w) / 2 + r + (a * position);
        float off = positionOffset * a;
        canvas.drawCircle(x + off, r, r, paint);
        float xx = x;
        if (off > a / 2)
            xx += (off - a / 2) * 2;
        canvas.drawCircle(xx, r, r, paint);

        canvas.drawRect(xx, 0, x + off, getHeight(), paint);
    }
}
