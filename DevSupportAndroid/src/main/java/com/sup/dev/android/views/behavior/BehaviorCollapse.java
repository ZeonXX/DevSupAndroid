package com.sup.dev.android.views.behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

public class BehaviorCollapse<V extends View> extends CoordinatorLayout.Behavior<V> {

    public BehaviorCollapse(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, V child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, V child, View dependency) {
        child.setScaleX(Math.max((1-(-dependency.getY()) / 255), 0));
        child.setScaleY(Math.max((1-(-dependency.getY()) / 255), 0));
        child.setVisibility(child.getScaleX() == 0?View.GONE:View.VISIBLE);
        return super.onDependentViewChanged(parent, child, dependency);
    }
}