package com.sup.dev.android.views.sheets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsView;

public abstract class BaseSheet extends FrameLayout {

    protected final Context viewContext;
    protected final View view;
    private final int fabId;
    private final boolean openOnFab;

    private FloatingActionButton vFab;

    private BottomSheetBehavior behavior;

    public BaseSheet(Context viewContext, AttributeSet attrs, @LayoutRes int res) {
        super(viewContext, attrs);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.BaseSheet, 0, 0);
        fabId = a.getResourceId(R.styleable.BaseSheet_Sheet_fabId, 0);
        openOnFab = a.getBoolean(R.styleable.BaseSheet_Sheet_openOnFab, false);
        a.recycle();

        this.viewContext = viewContext;
        this.view = ToolsView.inflate(viewContext, res);

        addView(view);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (behavior != null) return;

        this.behavior = BottomSheetBehavior.from(this);
        if(fabId != 0) {
            vFab = ToolsView.findViewOnParents(BaseSheet.this, fabId);
            if(openOnFab)vFab.setOnClickListener(v -> show());
        }


        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {

            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                BaseSheet.this.onStateChanged(newState);
                if (newState == BottomSheetBehavior.STATE_DRAGGING) onDragged();
                if (newState == BottomSheetBehavior.STATE_EXPANDED) onExpanded();
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) onCollapsed();
                if (newState == BottomSheetBehavior.STATE_HIDDEN) onHidden();
                if (newState == BottomSheetBehavior.STATE_SETTLING) onSettling();
            }

            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (vFab != null) vFab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
            }

        });
    }

    public <K extends BaseSheet> K show() {
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        return (K) this;
    }

    public void hide() {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    protected void onDragged() {

    }

    protected void onExpanded() {

    }

    protected void onCollapsed() {

    }

    protected void onHidden() {

    }

    protected void onSettling() {

    }

    protected void onStateChanged(int newState) {

    }


}
