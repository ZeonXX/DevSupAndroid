package com.sup.dev.android.views.views.layouts;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.tools.ToolsThreads;

public class LayoutMirrorSize extends FrameLayout {

    public static final int MODE_BIGGER_WH = 1;
    public static final int MODE_SMALLER_WH = 2;
    public static final int MODE_WHOM_BIGGER_W = 3;
    public static final int MODE_WHOM_SMALLER_W = 4;
    public static final int MODE_WHOM_BIGGER_H = 5;
    public static final int MODE_WHOM_SMALLER_H = 6;
    public static final int MODE_USE_1_IF_NO_ZERO = 7;
    public static final int MODE_USE_2_IF_NO_ZERO = 8;

    private int multiViewMode = MODE_USE_1_IF_NO_ZERO;
    private int mirrorViewId = 0;
    private int mirrorViewId_2 = 0;
    private float wPercent = 100;
    private float hPercent = 100;
    private View mirrorView;
    private View mirrorView_2;

    public LayoutMirrorSize(@NonNull Context context) {
        this(context, null);
    }

    public LayoutMirrorSize(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        SupAndroid.initEditMode(this);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LayoutMirrorSize, 0, 0);
        mirrorViewId = a.getResourceId(R.styleable.LayoutMirrorSize_LayoutMirrorSize_mirrorView, mirrorViewId);
        mirrorViewId_2 = a.getResourceId(R.styleable.LayoutMirrorSize_LayoutMirrorSize_mirrorView_2, mirrorViewId_2);
        wPercent = a.getInteger(R.styleable.LayoutMirrorSize_LayoutMirrorSize_wPercent, (int) wPercent);
        hPercent = a.getInteger(R.styleable.LayoutMirrorSize_LayoutMirrorSize_hPercent, (int) hPercent);
        multiViewMode = a.getInt(R.styleable.LayoutMirrorSize_LayoutMirrorSize_mode, multiViewMode);
        a.recycle();

    }

    public void setMirrorView(View mirrorView) {
        this.mirrorView = mirrorView;
        if (mirrorView == null) return;
        mirrorView.addOnLayoutChangeListener((view, i, i1, i2, i3, i4, i5, i6, i7) -> {
            ToolsThreads.main(true, () -> requestLayout());
        });
        requestLayout();
    }

    public void setMirrorView_2(View mirrorView_2) {
        this.mirrorView_2 = mirrorView_2;
        if (mirrorView_2 == null) return;
        mirrorView_2.addOnLayoutChangeListener((view, i, i1, i2, i3, i4, i5, i6, i7) -> {
            ToolsThreads.main(true, () -> requestLayout());
        });
        requestLayout();
    }

    public void setMode(int mode) {
        multiViewMode = mode;
        requestLayout();
    }

    private int w;
    private int h;

    private void prepareSizes(float w, float h) {
        this.w = (int) ((w / 100) * wPercent);
        this.h = (int) ((h / 100) * hPercent);
    }

    private boolean mirrorViewAvailable() {
        return mirrorView != null && mirrorView.getVisibility() == VISIBLE;
    }

    private boolean mirrorView_2_Available() {
        return mirrorView_2 != null && mirrorView_2.getVisibility() == VISIBLE;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (mirrorView == null && mirrorViewId != 0)
            setMirrorView(ToolsView.findViewOnParents(this, mirrorViewId));
        if (mirrorView_2 == null && mirrorViewId_2 != 0)
            setMirrorView_2(ToolsView.findViewOnParents(this, mirrorViewId_2));

        if (!mirrorViewAvailable() && !mirrorView_2_Available()) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY));
            return;
        }

        w = MeasureSpec.getSize(widthMeasureSpec);
        h = MeasureSpec.getSize(heightMeasureSpec);


        if (mirrorViewAvailable() && !mirrorView_2_Available())
            prepareSizes(mirrorView.getMeasuredWidth(), mirrorView.getMeasuredHeight());

        if (!mirrorViewAvailable() && mirrorView_2_Available())
            prepareSizes(mirrorView_2.getMeasuredWidth(), mirrorView_2.getMeasuredHeight());


        if (mirrorViewAvailable() && mirrorView_2_Available()) {

            float w1 = mirrorView.getMeasuredWidth();
            float h1 = mirrorView.getMeasuredHeight();
            float w2 = mirrorView_2.getMeasuredWidth();
            float h2 = mirrorView_2.getMeasuredHeight();

            if (multiViewMode == MODE_BIGGER_WH) prepareSizes(Math.max(w1, w2), Math.max(h1, h2));
            if (multiViewMode == MODE_SMALLER_WH) prepareSizes(Math.min(w1, w2), Math.min(h1, h2));
            if (multiViewMode == MODE_WHOM_BIGGER_W)
                prepareSizes(w2 > w1 ? w2 : w1, w2 > w1 ? h2 : h1);
            if (multiViewMode == MODE_WHOM_SMALLER_W)
                prepareSizes(w1 < w2 ? w1 : w2, w1 < w2 ? h1 : h2);
            if (multiViewMode == MODE_WHOM_BIGGER_H)
                prepareSizes(h2 > h1 ? w2 : w1, h2 > h1 ? h2 : h1);
            if (multiViewMode == MODE_WHOM_SMALLER_H)
                prepareSizes(h1 < h2 ? w1 : w2, h1 < h2 ? h1 : h2);
            if (multiViewMode == MODE_USE_1_IF_NO_ZERO)
                prepareSizes(w1 != 0 ? w1 : w2, h1 != 0 ? h1 : h2);
            if (multiViewMode == MODE_USE_2_IF_NO_ZERO)
                prepareSizes(w2 != 0 ? w2 : w1, h2 != 0 ? h2 : h1);

        }

        super.onMeasure(MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY));


    }

}
