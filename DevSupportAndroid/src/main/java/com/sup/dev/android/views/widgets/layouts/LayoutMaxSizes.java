package com.sup.dev.android.views.widgets.layouts;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsAndroid;
import com.sup.dev.android.utils.interfaces.UtilsView;

import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.UNSPECIFIED;

public class LayoutMaxSizes extends FrameLayout {

    private final UtilsAndroid utilsAndroid;
    private final UtilsView utilsView;

    private int maxWidth;
    private int maxHeight;
    private int reserveWidth;
    private int reserveHeight;
    private float maxWidthPercent;
    private float maxHeightPercent;
    private boolean alwaysMaxW = false;
    private boolean alwaysMaxH = false;
    private boolean cropEnabled = true;
    private boolean reversMaxValuesOnScreenRotation = false;
    private boolean useScreenWidthAsParent = false;
    private boolean useScreenHeightAsParent = false;

    public LayoutMaxSizes(Context context) {
        this(context, null);
    }

    public LayoutMaxSizes(Context context, AttributeSet attrs) {
        super(context, attrs);

        SupAndroid.initEditMode(this);
        utilsAndroid = SupAndroid.di.utilsAndroid();
        utilsView = SupAndroid.di.utilsView();

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
        a.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (!cropEnabled) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        boolean reverse = reversMaxValuesOnScreenRotation && utilsAndroid.isScreenLandscape();
        boolean uScreenW = reverse ? useScreenHeightAsParent : useScreenWidthAsParent;
        boolean uScreenH = reverse ? useScreenWidthAsParent : useScreenHeightAsParent;
        int w = uScreenW ? utilsAndroid.getScreenW() : MeasureSpec.getSize(widthMeasureSpec);
        int h = uScreenH ? utilsAndroid.getScreenH() : MeasureSpec.getSize(heightMeasureSpec);
        int wm = MeasureSpec.getMode(widthMeasureSpec);
        int hm = MeasureSpec.getMode(heightMeasureSpec);
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
            wm = EXACTLY;
        }

        if (maxH > 0 && (alMH || h > maxH + reserveH)) {
            h = maxH;
            hm = EXACTLY;
        }

        super.onMeasure(MeasureSpec.makeMeasureSpec(w, wm), MeasureSpec.makeMeasureSpec(h, hm));

        if (wm == UNSPECIFIED && hm == UNSPECIFIED) {
            measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), EXACTLY), MeasureSpec.makeMeasureSpec(getMeasuredHeight(), EXACTLY));
            return;
        }
        if (wm == UNSPECIFIED) measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), EXACTLY), MeasureSpec.makeMeasureSpec(h, hm));
        if (hm == UNSPECIFIED) measure(MeasureSpec.makeMeasureSpec(w, wm), MeasureSpec.makeMeasureSpec(getMeasuredHeight(), EXACTLY));
    }

    //
    //  Setters
    //

    public void setMaxWidth(int maxWidthDp) {
        this.maxWidth = utilsView.dpToPx(maxWidthDp);
        requestLayout();
    }

    public void setMaxHeight(int maxHeightDp) {
        this.maxHeight = utilsView.dpToPx(maxHeightDp);
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

    public void setCropEnabled(boolean cropEnabled) {
        this.cropEnabled = cropEnabled;
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

