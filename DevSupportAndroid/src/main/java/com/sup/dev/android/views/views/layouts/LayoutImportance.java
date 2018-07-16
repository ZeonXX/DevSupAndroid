package com.sup.dev.android.views.widgets.layouts;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sup.dev.android.androiddevsup.R;

import java.util.ArrayList;
import java.util.Collections;

public class LayoutImportance extends LinearLayout {

    private int lock;

    public LayoutImportance(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (lock == 0) for (int i = 0; i < getChildCount(); i++) getChildAt(i).setVisibility(VISIBLE);

        int w = MeasureSpec.getSize(widthMeasureSpec);

        super.onMeasure(MeasureSpec.makeMeasureSpec(w, MeasureSpec.UNSPECIFIED), heightMeasureSpec);

        if (getMeasuredWidth() > w) {

            ArrayList<View> children = new ArrayList<>();
            for (int i = 0; i < getChildCount(); i++) if (getChildAt(i).getVisibility() == VISIBLE) children.add(getChildAt(i));
            if(children.isEmpty())return;

            Collections.sort(children, (o1, o2) -> ((LayoutParams) o1.getLayoutParams()).importance - ((LayoutParams) o2.getLayoutParams()).importance);
            children.get(0).setVisibility(GONE);

            lock++;
            onMeasure(widthMeasureSpec, heightMeasureSpec);
            lock--;
        }

    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(getContext(), null);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p.width, p.height);
    }


    public static class LayoutParams extends LinearLayout.LayoutParams {

        public int importance;

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LayoutImportance_Layout);
            importance = a.getInt(R.styleable.LayoutImportance_Layout_LayoutImportance_Layout_importance, 0);
            a.recycle();
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

    }
}
