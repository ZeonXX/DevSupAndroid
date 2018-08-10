package com.sup.dev.android.views.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.sup.dev.android.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;

import java.util.ArrayList;

public class ViewGraph extends View {

    private final Paint paint = new Paint();
    private final ArrayList<Float> points = new ArrayList<>();

    private int textSize = ToolsView.spToPx(12);
    private int greedSize = ToolsView.dpToPx(1);
    private int greedColor = ToolsResources.getColor(R.color.grey_400);
    private int pointSize = ToolsView.dpToPx(4);
    private int pointColor = ToolsResources.getColor(R.color.red_700);
    private int lineSize = ToolsView.dpToPx(1);
    private int lineColor = ToolsResources.getColor(R.color.green_700);
    private boolean fill;
    private int fillColor;

    public ViewGraph(Context context) {
        this(context, null);
    }

    public ViewGraph(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int cellSizeW = getWidth() / points.size();
        float maxY = 0;
        float minY = 0;
        for (Float d : points) {
            maxY = maxY > d ? maxY : d;
            minY = minY < d ? minY : d;
        }
        float h = (maxY - minY) + 1;
        float cellSizeH = getHeight() / h;

        if (greedSize > 0) {
            paint.setStrokeWidth(greedSize);
            paint.setColor(greedColor);
            for (int x = 0; x < points.size() + 1; x++) canvas.drawLine(cellSizeW * x, 0, cellSizeW * x, getHeight(), paint);
            for (int y = 0; y < h + 1; y++) canvas.drawLine(0, cellSizeH * y, getWidth(), cellSizeH * y, paint);
        }

        if (lineSize > 0 && points.size() > 0) {
            paint.setStrokeWidth(lineSize);
            paint.setColor(lineColor);
            for (int i = 1; i < points.size(); i++) {
                float pX = (i-1) * cellSizeW + (cellSizeW / 2);
                float pY = (cellSizeH * h) - (points.get(i-1) * cellSizeH) - cellSizeH / 2;
                float x = i * cellSizeW + (cellSizeW / 2);
                float y = (cellSizeH * h) - (points.get(i) * cellSizeH) - cellSizeH / 2;
                canvas.drawLine(pX, pY, x, y, paint);
            }
        }

        if (pointSize > 0) {
            paint.setColor(pointColor);
            for (int i = 0; i < points.size(); i++) {
                float x = i * cellSizeW + (cellSizeW / 2);
                float y = (cellSizeH * h) - (points.get(i) * cellSizeH) - cellSizeH / 2;
                canvas.drawCircle(x, y, pointSize, paint);
            }

        }

    }

    public void addPoint(float value) {
        points.add(value);
    }


}
