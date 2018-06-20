package com.sup.dev.android.views.views.layouts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class LayoutChip extends FrameLayout {

    private Paint paint;
    private final Path path;

    public LayoutChip(@NonNull Context context) {
        this(context, null);
    }

    public LayoutChip(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        path = new Path();
        setWillNotDraw(false);
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(null);

        if (paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(0x00000000);
        }

        if (background != null && background instanceof ColorDrawable)
            paint.setColor(((ColorDrawable) background).getColor());

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (paint != null) canvas.drawPath(path, paint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        path.reset();
        if (getHeight() < getWidth()) {
            path.addArc(new RectF(0, 0, getHeight(), getHeight()), 90, 180);
            path.addArc(new RectF(getWidth() - getHeight(), 0, getWidth(), getHeight()), 270, 180);
            path.addRect(getHeight() / 2, 0, getWidth() - getHeight() / 2, getHeight(), Path.Direction.CCW);
        } else {
            path.addCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, Path.Direction.CCW);

        }
    }

}
