package com.sup.dev.android.views.widgets.layouts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.tools.ToolsAndroid;
import com.sup.dev.android.tools.ToolsPaint;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.libs.debug.Debug;

import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.UNSPECIFIED;

public class LayoutMaxSizes extends ViewGroup {

    private int maxWidth;
    private int maxHeight;
    private int reserveWidth;
    private int reserveHeight;
    private float maxWidthPercent;
    private float maxHeightPercent;
    private boolean alwaysMaxW = false;
    private boolean alwaysMaxH = false;
    private boolean reversMaxValuesOnScreenRotation = false;
    private boolean useScreenWidthAsParent = false;
    private boolean useScreenHeightAsParent = false;
    private boolean allowChildMaxW = false;
    private boolean allowChildMaxH = false;
    private int fadeWSize;
    private int fadeHSize;
    private int fadeColor;

    private boolean isCroppedW;
    private boolean isCroppedH;

    public LayoutMaxSizes(Context context) {
        this(context, null);
    }

    public LayoutMaxSizes(Context context, AttributeSet attrs) {
        super(context, attrs);

        SupAndroid.initEditMode(this);

        setWillNotDraw(false);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LayoutMaxSizes, 0, 0);
        maxWidth = (int) a.getDimension(R.styleable.LayoutMaxSizes_LayoutMaxSizes_maxWidth, maxWidth);
        maxHeight = (int) a.getDimension(R.styleable.LayoutMaxSizes_LayoutMaxSizes_maxHeight, maxHeight);
        reserveWidth = (int) a.getDimension(R.styleable.LayoutMaxSizes_LayoutMaxSizes_reserveWidth, reserveWidth);
        reserveHeight = (int) a.getDimension(R.styleable.LayoutMaxSizes_LayoutMaxSizes_reserveHeight, reserveHeight);
        maxWidthPercent = a.getFloat(R.styleable.LayoutMaxSizes_LayoutMaxSizes_maxWidthParentPercent, maxWidthPercent);
        maxHeightPercent = a.getFloat(R.styleable.LayoutMaxSizes_LayoutMaxSizes_maxHeightParentPercent, maxHeightPercent);
        alwaysMaxW = a.getBoolean(R.styleable.LayoutMaxSizes_LayoutMaxSizes_alwaysMaxW, alwaysMaxW);
        alwaysMaxH = a.getBoolean(R.styleable.LayoutMaxSizes_LayoutMaxSizes_alwaysMaxH, alwaysMaxH);
        reversMaxValuesOnScreenRotation = a.getBoolean(R.styleable.LayoutMaxSizes_LayoutMaxSizes_reversMaxValuesOnScreenRotation, reversMaxValuesOnScreenRotation);
        useScreenWidthAsParent = a.getBoolean(R.styleable.LayoutMaxSizes_LayoutMaxSizes_useScreenWidthAsParent, useScreenWidthAsParent);
        useScreenHeightAsParent = a.getBoolean(R.styleable.LayoutMaxSizes_LayoutMaxSizes_useScreenHeightAsParent, useScreenHeightAsParent);
        fadeWSize = (int) a.getDimension(R.styleable.LayoutMaxSizes_LayoutMaxSizes_fadeWSize, fadeWSize);
        fadeHSize = (int) a.getDimension(R.styleable.LayoutMaxSizes_LayoutMaxSizes_fadeHSize, fadeHSize);
        fadeColor = a.getColor(R.styleable.LayoutMaxSizes_LayoutMaxSizes_fadeColor, fadeColor);
        allowChildMaxW = a.getBoolean(R.styleable.LayoutMaxSizes_LayoutMaxSizes_allowChildMaxW, allowChildMaxW);
        allowChildMaxH = a.getBoolean(R.styleable.LayoutMaxSizes_LayoutMaxSizes_allowChildMaxH, allowChildMaxH);
        a.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++)
            getChildAt(i).layout(0, 0, getChildAt(i).getMeasuredWidth(), getChildAt(i).getMeasuredHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        boolean reverse = reversMaxValuesOnScreenRotation && ToolsAndroid.isScreenLandscape();
        boolean uScreenW = reverse ? useScreenHeightAsParent : useScreenWidthAsParent;
        boolean uScreenH = reverse ? useScreenWidthAsParent : useScreenHeightAsParent;
        int w = uScreenW ? ToolsAndroid.getScreenW() : MeasureSpec.getSize(widthMeasureSpec);
        int h = uScreenH ? ToolsAndroid.getScreenH() : MeasureSpec.getSize(heightMeasureSpec);
        int maxW = reverse ? maxHeight : maxWidth;
        int maxH = reverse ? maxWidth : maxHeight;
        int reserveW = reverse ? reserveHeight : reserveWidth;
        int reserveH = reverse ? reserveWidth : reserveHeight;
        float maxWPer = reverse ? maxHeightPercent : maxWidthPercent;
        float maxHPer = reverse ? maxWidthPercent : maxHeightPercent;
        boolean alMW = reverse ? alwaysMaxH : alwaysMaxW;
        boolean alMH = reverse ? alwaysMaxW : alwaysMaxH;

        if (maxWPer != 0) {
            int arg = (int) (w / 100f * maxWPer);
            maxW = maxW == 0 || maxW > arg ? arg : maxW;
        }

        if (maxHPer != 0) {
            int arg = (int) (h / 100f * maxHPer);
            maxH = maxH == 0 || maxH > arg ? arg : maxH;
        }

        if (maxW > 0 && (alMW || w > maxW + reserveW)) {
            w = maxW;
            isCroppedW = true;
        } else {
            isCroppedW = false;
        }

        if (maxH > 0 && (alMH || h > maxH + reserveH)) {
            h = maxH;
            isCroppedH = true;
        } else {
            isCroppedH = false;
        }

        Debug.log(alMW, maxW, w, "   ", alMH, maxH, h);

        int maxChildW = 0;
        int maxChildH = 0;
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).measure(MeasureSpec.makeMeasureSpec(w, alMW ? EXACTLY : allowChildMaxW ? UNSPECIFIED : AT_MOST),
                    MeasureSpec.makeMeasureSpec(h, alMH ? EXACTLY : allowChildMaxH ? UNSPECIFIED : AT_MOST));
            maxChildW = Math.max(getChildAt(i).getMeasuredWidth(), maxChildW);
            maxChildH = Math.max(getChildAt(i).getMeasuredHeight(), maxChildH);
        }

        setMeasuredDimension(alMW ? maxW : Math.min(w, maxChildW), alMH ? maxH : Math.min(h, maxChildH));

    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);


        if (fadeColor == 0 && (fadeHSize != 0 || fadeWSize != 0) && (isCroppedH || isCroppedW))
            fadeColor = ToolsView.getRootBackground(this);

        if (fadeWSize != 0 && isCroppedW && fadeColor != 0) ToolsPaint.gradientLineLeftRight(canvas, fadeColor, fadeWSize);
        if (fadeHSize != 0 && isCroppedH && fadeColor != 0) ToolsPaint.gradientLineBottomTop(canvas, fadeColor, fadeHSize);

    }

    //
    //  Setters
    //


    public void setFadeHSize(int dp) {
        this.fadeHSize = ToolsView.dpToPx(dp);
        invalidate();
    }

    public void setFadeWSize(int dp) {
        this.fadeWSize = ToolsView.dpToPx(dp);
        invalidate();
    }

    public void setFadeColor(int fadeColor) {
        this.fadeColor = fadeColor;
        invalidate();
    }

    public void setMaxWidth(int maxWidthDp) {
        this.maxWidth = ToolsView.dpToPx(maxWidthDp);
        requestLayout();
    }

    public void setMaxHeight(int maxHeightDp) {
        this.maxHeight = ToolsView.dpToPx(maxHeightDp);
        requestLayout();
    }

    public void setAlwaysMaxW(boolean b) {
        this.alwaysMaxW = b;
        requestLayout();
    }

    public void setAlwaysMaxH(boolean b) {
        this.alwaysMaxH = b;
        requestLayout();
    }

    public void setMaxHeightParentPercent(float maxHeightPercent) {
        this.maxHeightPercent = maxHeightPercent;
        requestLayout();
    }

    public void setMaxWidthParentPercent(float maxWidthPercent) {
        this.maxWidthPercent = maxWidthPercent;
        requestLayout();
    }

    public void setReversMaxValuesOnScreenRotation(boolean reversMaxValuesOnScreenRotation) {
        this.reversMaxValuesOnScreenRotation = reversMaxValuesOnScreenRotation;
        requestLayout();
    }

    public void setUseScreenWidthAsParent(boolean useScreenWidthAsParent) {
        this.useScreenWidthAsParent = useScreenWidthAsParent;
        requestLayout();
    }

    public void setUseScreenHeightAsParent(boolean useScreenHeightAsParent) {
        this.useScreenHeightAsParent = useScreenHeightAsParent;
        requestLayout();
    }

    //
    //  Getters
    //

    public int getMaxWidth() {
        return maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }


}

