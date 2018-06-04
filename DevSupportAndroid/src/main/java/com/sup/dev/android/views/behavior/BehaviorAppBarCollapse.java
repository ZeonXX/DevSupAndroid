package com.sup.dev.android.views.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

public class BehaviorAppBarCollapse<V extends View> extends BehaviorCollapse<V> {

    public BehaviorAppBarCollapse(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, V child, View dependency) {
        child.setY(dependency.getY() + dependency.getHeight() - child.getHeight() / 2);
        return super.onDependentViewChanged(parent, child, dependency);
    }
}