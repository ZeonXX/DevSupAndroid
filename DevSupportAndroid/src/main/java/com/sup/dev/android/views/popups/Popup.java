package com.sup.dev.android.views.popups;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.PopupWindow;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsAndroid;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.libs.debug.Debug;

public class Popup extends PopupWindow {

    private final View anchor;

    public Popup(View anchor, @LayoutRes int res) {
        this(anchor, ToolsView.inflate(anchor.getContext(), res));
    }

    public Popup(Context viewContext, @LayoutRes int res) {
        this(null, ToolsView.inflate(viewContext, res));
    }

    public Popup(View anchor, View view) {
        super(view.getContext());
        this.anchor = anchor;
        setBackgroundDrawable(new ColorDrawable(ToolsResources.getPrimaryColor(view.getContext())));
        setContentView(view);
        setOutsideTouchable(true);
        setFocusable(true);

        init();
    }

    protected void init(){

    }

    public final <T extends View> T findViewById(@IdRes int id) {
        return getContentView().findViewById(id);
    }

    public void showAtLocation(View parent, int gravity) {
        super.showAtLocation(parent, gravity, 0, 0);
    }

    public void showAsDropDownOverlay(View anchor) {
        showAsDropDown(anchor, 0, -anchor.getHeight());
    }

    public void show(){
        showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        onPreShow();
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        onPreShow();
        super.showAtLocation(parent, gravity, x, y);
    }

    public void hide(){
        dismiss();
    }


    @CallSuper
    protected void onPreShow(){
        getContentView().measure(ToolsAndroid.getScreenW(), ToolsAndroid.getScreenH());
        setWidth(getContentView().getMeasuredWidth());
        setHeight(getContentView().getMeasuredHeight());
    }

}
