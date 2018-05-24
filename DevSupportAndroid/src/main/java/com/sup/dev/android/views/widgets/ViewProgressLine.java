package com.sup.dev.android.views.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ViewProgressLine extends View {

    private final Paint paint;
    private int progress;

    public ViewProgressLine(Context context) {
        this(context, null);
    }

    public ViewProgressLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int r = Math.min(getWidth(), getHeight());

        canvas.drawCircle(r, r, r, paint);
        canvas.drawCircle(getWidth() - r, r, r, paint);
        canvas.drawRect(r, 0, getWidth() - r, getHeight(), paint);
    }

    public void setProgress(long value, long max) {
        setProgress((int) (100D / (max / (double) value)));
    }

    public void setProgress(double value, double max) {
        setProgress((int) (100D / (max / value)));
    }

    public void setProgress(int percent) {
        this.progress = percent;
        invalidate();
    }

    public void setLineColor(int color) {
        paint.setColor(color);
        invalidate();
    }
}
