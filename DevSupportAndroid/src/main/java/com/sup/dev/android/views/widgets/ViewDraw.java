package com.sup.dev.android.views.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.sup.dev.java.classes.callbacks.simple.Callback1;

public class ViewDraw extends View {

    private Callback1<Canvas> onDraw;

    public ViewDraw(Context context) {
        this(context, null);
    }

    public ViewDraw(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public void setOnDraw(Callback1<Canvas> onDraw) {
        this.onDraw = onDraw;
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (onDraw != null) onDraw.callback(canvas);
    }

}
