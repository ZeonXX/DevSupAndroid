package com.sup.dev.android.views.views.layouts;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sup.dev.android.androiddevsup.R;

import static android.view.View.MeasureSpec.UNSPECIFIED;

public class LayoutAspectRatio extends FrameLayout {

    private float rw = 1;
    private float rh = 1;

    public LayoutAspectRatio(@NonNull Context context) {
        this(context, null);
    }

    public LayoutAspectRatio(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LayoutAspectRatio, 0, 0);
        rw = a.getFloat(R.styleable.LayoutAspectRatio_LayoutAspectRatio_w, rw);
        rh = a.getFloat(R.styleable.LayoutAspectRatio_LayoutAspectRatio_h, rh);
        a.recycle();

    }

    public void setRatio(float rw, float rh) {
        this.rw = rw;
        this.rh = rh;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (rw <= 0 || rh <= 0) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY));
            return;
        }

        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT)
            getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT)
            getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int wm = MeasureSpec.getMode(widthMeasureSpec);
        int hm = MeasureSpec.getMode(heightMeasureSpec);

        if (wm == UNSPECIFIED) w = h;
        if (hm == UNSPECIFIED) h = w;

        float aw = w / rw;
        float ah = h / rh;
        float insW = rw * (ah < aw ? ah : aw);
        float insH = rh * (ah < aw ? ah : aw);

        super.onMeasure(MeasureSpec.makeMeasureSpec((int) insW, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec((int) insH, MeasureSpec.EXACTLY));
    }

}
