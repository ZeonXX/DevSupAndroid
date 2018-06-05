package com.sup.dev.android.tools;

import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;

public class ToolsPaint {

    public static void gradientLineBottomTop(Canvas canvas, int color, int size) {
        gradientLine(canvas, color, size, GradientDrawable.Orientation.BOTTOM_TOP);
    }

    public static void gradientLineLeftRight(Canvas canvas, int color, int size) {
        gradientLine(canvas, color, size, GradientDrawable.Orientation.LEFT_RIGHT);
    }

    private static void gradientLine(Canvas canvas, int color, int size, GradientDrawable.Orientation orientation) {
        GradientDrawable gradientDrawable = new GradientDrawable(
                orientation,
                new int[]{color, ToolsColor.setAlpha(0, color)});
        gradientDrawable.setSize(canvas.getWidth(), size);
        gradientDrawable.setBounds(0, canvas.getHeight() - size, canvas.getWidth(), canvas.getHeight());
        gradientDrawable.draw(canvas);
    }
