package com.sup.dev.android.views.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.tools.ToolsResources;

public class ViewProgressLine extends View {

    private final Paint paint;
    private final Path path;

    private float progressPercent;
    private int colorProgress;
    private int colorBackground;

    public ViewProgressLine(Context context) {
        this(context, null);
    }

    public ViewProgressLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        SupAndroid.initEditMode(this);
        colorBackground = ToolsResources.getColor(R.color.focus_dark);
        colorProgress = ToolsResources.getAccentColor(context);


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewProgressLine, 0, 0);
        int progress = a.getInteger(R.styleable.ViewProgressLine_ViewProgressLine_progress, 0);
        colorProgress = a.getColor(R.styleable.ViewProgressLine_ViewProgressLine_color, colorProgress);
        colorBackground = a.getColor(R.styleable.ViewProgressLine_ViewProgressLine_colorBackground, colorBackground);
        a.recycle();

        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        setProgress(progress);
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

        paint.setColor(colorBackground);
        canvas.drawPath(path, paint);

        if (progressPercent > 0) {
            paint.setColor(colorProgress);

            float end = ((getWidth()-r) / 100f * progressPercent);

            canvas.drawCircle(r, r, r, paint);
            if (end > r) {
                canvas.drawCircle(end, r, r, paint);
                canvas.drawRect(r, 0, end, getHeight(), paint);
            }
        }

    }

    public void setProgress(long value, long max) {
        setProgress(100F * ((float) value / max));
    }

    public void setProgress(float value, float max) {
        setProgress(100F / (max / value));
    }

    public void setProgress(float percent) {
        this.progressPercent = percent;
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
