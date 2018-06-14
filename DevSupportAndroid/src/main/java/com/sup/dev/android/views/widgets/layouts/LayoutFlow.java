package com.sup.dev.android.views.widgets.layouts;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.MainThread;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.sup.dev.android.androiddevsup.R;

public class LayoutFlow extends ViewGroup {

    private int mHorizontalSpacing;
    private int mVerticalSpacing;

    @MainThread
    public LayoutFlow(Context context) {
        this(context, null);
    }

    @MainThread
    public LayoutFlow(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LayoutFlow);
        mHorizontalSpacing = a.getDimensionPixelSize(R.styleable.LayoutFlow_LayoutFlow_horizontal, 0);
        mVerticalSpacing = a.getDimensionPixelSize(R.styleable.LayoutFlow_LayoutFlow_vertical, 0);

    }

    public void setHorizontalSpacing(int mHorizontalSpacing) {
        this.mHorizontalSpacing = mHorizontalSpacing;
    }

    public void setVerticalSpacing(int mVerticalSpacing) {
        this.mVerticalSpacing = mVerticalSpacing;
    }

    @Override
    @MainThread
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthLimit = MeasureSpec.getSize(widthMeasureSpec) - getPaddingRight();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        boolean growHeight = widthMode != MeasureSpec.UNSPECIFIED;
        int width = 0;
        int currentWidth = getPaddingLeft();
        int currentHeight = getPaddingTop();
        int maxChildHeight = 0;
        boolean breakLine = false;
        boolean newLine = false;
        int spacing = 0;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            spacing = mHorizontalSpacing;

            if (lp.horizontalSpacing >= 1)
                spacing = lp.horizontalSpacing;


            if (growHeight && (breakLine || ((currentWidth + child.getMeasuredWidth()) > widthLimit))) {
                newLine = true;
                currentHeight += maxChildHeight + mVerticalSpacing;

                width = Math.max(width, currentWidth - spacing);

                currentWidth = getPaddingLeft();
                maxChildHeight = 0;
            } else {
                newLine = false;
            }

            maxChildHeight = Math.max(maxChildHeight, child.getMeasuredHeight());

            lp.x = currentWidth;
            lp.y = currentHeight;

            currentWidth += child.getMeasuredWidth() + spacing;

            breakLine = lp.breakLine;
        }

        if (!newLine)
            width = Math.max(width, currentWidth - spacing);
        width += getPaddingRight();
        int height = currentHeight + maxChildHeight + getPaddingBottom();

        setMeasuredDimension(resolveSize(width, widthMeasureSpec),
                resolveSize(height, heightMeasureSpec));

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            child.layout(lp.x, lp.y, lp.x + child.getMeasuredWidth(), lp.y + child.getMeasuredHeight());
        }
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p.width, p.height);
    }


    public static class LayoutParams extends ViewGroup.LayoutParams {
        public int horizontalSpacing;
        public boolean breakLine;
        int x;
        int y;

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LayoutFlow_LayoutParams);
            try {
                horizontalSpacing = a.getDimensionPixelSize(R.styleable.LayoutFlow_LayoutParams_LayoutFlow_LayoutParams_horizontalSpacing, 0);
                breakLine = a.getBoolean(R.styleable.LayoutFlow_LayoutParams_LayoutFlow_LayoutParams_horizontalSpacing, false);
            } finally {
                a.recycle();
            }
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }
    }
}