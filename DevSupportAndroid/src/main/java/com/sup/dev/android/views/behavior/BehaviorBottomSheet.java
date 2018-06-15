package com.sup.dev.android.views.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class BehaviorBottomSheet<V extends View> extends BottomSheetBehavior<V>{

    private boolean canCollapse = false;

    public BehaviorBottomSheet() {
        super();
    }

    public BehaviorBottomSheet(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, V child, MotionEvent event) {
        return canCollapse && super.onInterceptTouchEvent(parent, child, event);
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, V child, MotionEvent event) {
        return canCollapse && super.onTouchEvent(parent, child, event);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return canCollapse && super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, V child, View target, int dx, int dy, int[] consumed) {
        if(canCollapse) super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, V child, View target) {
        if(canCollapse) super.onStopNestedScroll(coordinatorLayout, child, target);
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, V child, View target, float velocityX, float velocityY) {
        return canCollapse && super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    //
    //  Setters
    //

    public void setCanColapse(boolean canCollapse) {
        this.canCollapse = canCollapse;
    }
}


