package com.sup.dev.android.views.widgets;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;

public class ViewTextRounded extends android.support.v7.widget.AppCompatTextView  {

    public ViewTextRounded(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = (int) Math.ceil(getMaxLineWidth(getLayout()));
        int height = getMeasuredHeight();
        setMeasuredDimension(width, height);
    }

    private float getMaxLineWidth(Layout layout) {
        float maximumWidth = 0.0f;
        int lines = layout.getLineCount();
        for (int i = 0; i < lines; i++) {
            maximumWidth = Math.max(layout.getLineWidth(i), maximumWidth);
        }

        return maximumWidth;
    }
}