package com.sup.dev.android.views.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.sup.dev.android.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsText;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.classes.providers.Provider1;
import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.tools.ToolsMath;
import com.sup.dev.java.tools.ToolsThreads;

import java.util.ArrayList;

public class ViewGraph extends View {

    private final Paint paint = new Paint();
    private final ArrayList<Float> points = new ArrayList<>();
    private HorizontalScrollView vScroll;

    private int offsetViewBottom = ToolsView.dpToPx(24);
    private int offsetViewLeft = ToolsView.dpToPx(24);
    private int offsetViewTop = ToolsView.dpToPx(24);
    private int minPointsCount = 0;
    private int maxPointsCount = 0;
    private int minTopY = 0;
    private int greedYSize = ToolsView.dpToPx(1);
    private int greedYFrequency = 10;
    private int greedXSize = ToolsView.dpToPx(1);
    private int greedXFrequency = 10;
    private int greedColor = ToolsResources.getColor(R.color.grey_400);
    private int pointSize = ToolsView.dpToPx(2);
    private int pointColor = ToolsResources.getColor(R.color.red_700);
    private int lineSize = ToolsView.dpToPx(4);
    private int lineColor = ToolsResources.getColor(R.color.red_700);
    private int textSize = ToolsView.spToPx(8);

    private Provider1<Float, String> providerMaskX;
    private Provider1<Float, String> providerMaskY;

    public ViewGraph(Context context) {
        this(context, null);
    }

    public ViewGraph(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float h = 0;
        float wPoints = points.size() > minPointsCount ? points.size() : minPointsCount;
        for (Float d : points) h = h > d ? h : d;
        h = h > minTopY ? h : minTopY;
        float cellSizeH = (getHeight() - offsetViewBottom - offsetViewTop) / h;
        float cellSizeW = (getWidth() - offsetViewLeft) / wPoints;

        if (greedXSize > 0 || greedYSize > 0) {
            paint.setColor(greedColor);
            paint.setStrokeWidth(ToolsMath.max(greedXSize, greedYSize));
            canvas.drawLine(offsetViewLeft, offsetViewTop, getWidth(), offsetViewTop, paint);
            canvas.drawLine(offsetViewLeft, offsetViewTop, offsetViewLeft, getHeight() - offsetViewBottom, paint);
            canvas.drawLine(getWidth(), getHeight() - offsetViewBottom, offsetViewLeft, getHeight() - offsetViewBottom, paint);
            canvas.drawLine(getWidth(), getHeight() - offsetViewBottom, getWidth(), offsetViewTop, paint);
        }

        if (greedYSize > 0) {
            paint.setColor(greedColor);
            paint.setStrokeWidth(greedYSize);
            for (int y = 0; y < h + 1; y += greedYFrequency) {
                canvas.drawLine(offsetViewLeft, cellSizeH * y + offsetViewTop, getWidth(), cellSizeH * y + offsetViewTop, paint);
                if (y < h) canvas.drawText(providerMaskY.provide(h - y),
                        offsetViewLeft - ToolsText.getStringWidth(paint.getTypeface(), textSize, providerMaskY.provide(h - y)) - ToolsView.dpToPx(4),
                        cellSizeH * y + ToolsText.getStringHeight(paint.getTypeface(), textSize) / 2 + offsetViewTop,
                        paint);
            }

        }

        if (greedXSize > 0) {
            paint.setColor(greedColor);
            paint.setStrokeWidth(greedXSize);
            for (float x = 0; x < wPoints + 1; x += greedXFrequency) {
                canvas.drawLine(cellSizeW * x + offsetViewLeft, offsetViewTop, cellSizeW * x + offsetViewLeft, getHeight() - offsetViewBottom, paint);
                if (x > 0) canvas.drawText(providerMaskX.provide(x),
                        cellSizeW * x - ToolsText.getStringWidth(paint.getTypeface(), textSize, providerMaskX.provide(x)) / 2 + offsetViewLeft,
                        getHeight() - offsetViewBottom + ToolsText.getStringHeight(paint.getTypeface(), textSize) + ToolsView.dpToPx(4),
                        paint);
            }
        }

        if (lineSize > 0) {
            paint.setStrokeWidth(lineSize);
            paint.setColor(lineColor);
            for (int i = 1; i < points.size(); i++) {
                float pX = (i - 1) * cellSizeW + (cellSizeW / 2) + offsetViewLeft;
                float pY = (cellSizeH * h) - (points.get(i - 1) * cellSizeH) - cellSizeH / 2 + offsetViewTop;
                float x = i * cellSizeW + (cellSizeW / 2) + offsetViewLeft;
                float y = (cellSizeH * h) - (points.get(i) * cellSizeH) - cellSizeH / 2 + offsetViewTop;
                canvas.drawLine(pX, pY, x, y, paint);
            }
        }

        if (pointSize > 0) {
            paint.setColor(pointColor);
            for (int i = 0; i < points.size(); i++) {
                float x = i * cellSizeW + (cellSizeW / 2) + offsetViewLeft;
                float y = (cellSizeH * h) - (points.get(i) * cellSizeH) - cellSizeH / 2 + offsetViewTop;
                Debug.log("Draw point: i="+i+" v=" + points.get(i) +" x="+x+" y="+y +" " + (cellSizeH * h) +" " + (points.get(i) * cellSizeH));
                canvas.drawCircle(x, y, pointSize, paint);
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);

        float wPoints = points.size() > minPointsCount ? points.size() : minPointsCount;
        float cellSizeW = (w - offsetViewLeft) / (wPoints < maxPointsCount ? wPoints : maxPointsCount);

        super.onMeasure(MeasureSpec.makeMeasureSpec((int) (cellSizeW * wPoints + offsetViewLeft), MeasureSpec.EXACTLY), heightMeasureSpec);

        ToolsThreads.main(true, () -> {
            if (vScroll != null && getMeasuredWidth() - (vScroll.getScrollX() + vScroll.getWidth()) < ToolsView.dpToPx(15))
                vScroll.scrollTo(getMeasuredWidth(), vScroll.getScrollY());
        });
    }

    public void clear() {
        points.clear();
        requestLayout();
        invalidate();
    }

    public void addPoint(float value) {
        points.add(value);
        requestLayout();
        invalidate();
    }

    //
    //  Setters
    //


    public void setScroll(HorizontalScrollView vScroll) {
        this.vScroll = vScroll;
        requestLayout();
    }

    public void setMaxX(int maxPointsCount) {
        this.maxPointsCount = maxPointsCount;
        requestLayout();
    }

    public void setMinY(int minTopY) {
        this.minTopY = minTopY;
        requestLayout();
    }

    public void setMinX(int minPointsCount) {
        this.minPointsCount = minPointsCount;
        requestLayout();
    }

    public void setYFrequency(int greedYFrequency) {
        this.greedYFrequency = greedYFrequency;
        requestLayout();
    }

    public void setGreedYSize(int greedYSize) {
        this.greedYSize = greedYSize;
        requestLayout();
    }

    public void setXFrequency(int greedXFrequency) {
        this.greedXFrequency = greedXFrequency;
        requestLayout();
    }

    public void setGreedXSize(int greedXSize) {
        this.greedXSize = greedXSize;
        requestLayout();
    }

    public void setGreedColor(int greedColor) {
        this.greedColor = greedColor;
        requestLayout();
    }

    public void setProviderMaskY(Provider1<Float, String> providerMaskY) {
        this.providerMaskY = providerMaskY;
        requestLayout();
    }

    public void setProviderMaskX(Provider1<Float, String> providerMaskX) {
        this.providerMaskX = providerMaskX;
        requestLayout();
    }
}