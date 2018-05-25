package com.sup.dev.android.views.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.java.libs.debug.Debug;

public class ViewProgressLine extends View {

    private final Paint paint;
    private final Path path;

    private double progress;
    private int colorProgress;
    private int colorBackground;

    public ViewProgressLine(Context context) {
        this(context, null);
    }

    public ViewProgressLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        SupAndroid.initEditMode(this);
        colorBackground = ToolsResources.getColor(R.color.focus_dark);

        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        recreateChip();
    }

    private void recreateChip() {

        path.reset();

        path.addArc(new RectF(0, 0, getHeight(), getHeight()), 90, 180);
        path.addArc(new RectF(getWidth() - getHeight(), 0, getWidth(), getHeight()), 270, 180);
        path.addRect(getHeight() / 2, 0, getWidth() - getHeight() / 2, getHeight(), Path.Direction.CCW);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int r = Math.min(getWidth(), getHeight()) / 2;

        Debug.log(getWidth());

        paint.setColor(colorBackground);
        canvas.drawPath(path, paint);

        if (progress > 0) {
            paint.setColor(colorProgress);

            int end = (int) (getWidth() / 100 * progress);

            canvas.drawCircle(r, r, r, paint);
            if (end > r * 2) {
                canvas.drawCircle(end - r, r, r, paint);
                canvas.drawRect(r, 0, end - r, getHeight(), paint);
            }
        }

    }

    public void setProgress(long value, long max) {
        setProgress(100D * ((double) value / max));
    }

    public void setProgress(double value, double max) {
        setProgress(100D / (max / value));
    }

    public void setProgress(double percent) {
        this.progress = percent;
        invalidate();
    }

    public void setLineColorR(int res) {
        setLineColor(ToolsResources.getColor(res));
    }

    public void setLineColor(int color) {
        colorProgress = color;
        invalidate();
    }

    public void setLineBackgroundColorR(int res) {
        setLineBackgroundColor(ToolsResources.getColor(res));
    }

    public void setLineBackgroundColor(int color) {
        colorBackground = color;
        invalidate();
    }


}
