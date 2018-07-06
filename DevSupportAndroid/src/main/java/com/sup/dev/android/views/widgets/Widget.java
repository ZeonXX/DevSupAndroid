package com.sup.dev.android.views.widgets;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.libs.screens.NavigationAction;
import com.sup.dev.android.libs.screens.Navigator;
import com.sup.dev.android.tools.ToolsAndroid;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.cards.CardWidget;
import com.sup.dev.android.views.dialogs.DialogSheetWidget;
import com.sup.dev.android.views.dialogs.DialogWidget;
import com.sup.dev.android.views.popup.PopupWidget;
import com.sup.dev.android.views.screens.SWidget;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.providers.Provider;
import com.sup.dev.java.tools.ToolsThreads;

public abstract class Widget {

    protected final View view;
    protected final TextView vTitle;

    private Callback1<Widget> onHide;
    private boolean enabled = true;
    private boolean cancelable = true;
    protected WidgetViewWrapper viewWrapper;

    public Widget(int layoutRes) {
        view = layoutRes > 0 ? ToolsView.inflate(layoutRes) : instanceView();
        vTitle = findViewById(R.id.title);

        if (vTitle != null) {
            vTitle.setText(null);
            vTitle.setVisibility(View.GONE);
        }
    }

    protected View instanceView() {
        return null;
    }

    public void hide() {
        ToolsThreads.main(() -> {
            if (viewWrapper != null) viewWrapper.hideWidget();
        });
    }

    protected <K extends View> K findViewById(@IdRes int id) {
        return view.findViewById(id);
    }

    //
    //  Callbacks
    //

    public boolean onTryCancelOnTouchOutside(){
        return true;
    }

    @CallSuper
    public void onShow() {
        if (vTitle != null) {
            ToolsView.setTextOrGone(vTitle, vTitle.getText());
            if (viewWrapper instanceof SWidget) {
                vTitle.setVisibility(View.GONE);
                ((SWidget) viewWrapper).setTitle(vTitle.getText().toString());
            }
        }
    }

    @CallSuper
    public void onHide() {
        if (onHide != null) onHide.callback(this);
    }

    //
    //  Setters
    //

    public <K extends Widget> K setTitle(@StringRes int title) {
        return setTitle(ToolsResources.getString(title));
    }

    public <K extends Widget> K setTitle(String title) {
        if (vTitle != null) ToolsView.setTextOrGone(vTitle, title);
        return (K) this;
    }

    public <K extends Widget> K setTitleBackgroundColorRes(@ColorRes int color) {
        return setTitleBackgroundColor(ToolsResources.getColor(color));
    }
    public <K extends Widget> K setTitleBackgroundColor(@ColorInt int color) {
        if (vTitle != null) vTitle.setBackgroundColor(color);
        return (K) this;
    }

    public <K extends Widget> K setOnHide(Callback1<Widget> onHide) {
        this.onHide = onHide;
        return (K) this;
    }

    public <K extends Widget> K setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (vTitle != null) vTitle.setEnabled(enabled);
        if (viewWrapper != null) viewWrapper.setWidgetEnabled(enabled);
        return (K) this;
    }

    public <K extends Widget> K setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        if (viewWrapper != null) viewWrapper.setWidgetCancelable(cancelable);
        return (K) this;
    }

    //
    //  Getters
    //

    protected Context getContext() {
        return view.getContext();
    }

    public View getView() {
        return view;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    //
    //  Support
    //

    public DialogSheetWidget asSheet() {
        DialogSheetWidget sheet = new DialogSheetWidget(this);
        this.viewWrapper = sheet;
        return sheet;
    }

    public DialogSheetWidget asSheetShow() {
        DialogSheetWidget sheet = asSheet();
        sheet.show();
        return sheet;
    }

    public DialogWidget asDialog() {
        DialogWidget dialog = new DialogWidget(this);
        this.viewWrapper = dialog;
        return dialog;
    }

    public DialogWidget asDialogShow() {
        DialogWidget dialog = asDialog();
        dialog.show();
        return dialog;
    }

    public PopupWidget asPopup() {
        PopupWidget popup = new PopupWidget(this);
        this.viewWrapper = popup;
        return popup;
    }

    public PopupWidget asPopupShow(View view) {
        PopupWidget popup = asPopup();
        popup.show(view);
        return popup;
    }

    public PopupWidget asPopupShow(View view, int x, int y) {
        PopupWidget popup = asPopup();
        popup.show(view, x, y);
        return popup;
    }

    public Widget showPopupWhenClick(View view) {
        return showPopupWhenClick(view, null);
    }

    public Widget showPopupWhenClick(View view, Provider<Boolean> willShow) {
        ToolsView.setOnClickCoordinates(view, (view1, x, y) -> {
            if (willShow == null || willShow.provide()) asPopup().show(view1, x, y);
        });
        return this;
    }

    public Widget showPopupWhenLongClick(View view) {
        ToolsView.setOnLongClickCoordinates(view, (view1, x, y) -> asPopup().show(view1, x, y));
        return this;
    }

    public Widget showPopupWhenClickAndLongClick(View view, Provider<Boolean> willShowClicik) {
        return showPopupWhenClickAndLongClick(view, willShowClicik, null);
    }

    public Widget showPopupWhenClickAndLongClick(View view, Provider<Boolean> willShowClick, Provider<Boolean> willShowLongClick) {
        ToolsView.setOnClickAndLongClickCoordinates(view, (view1, x, y, isClick) -> {
            if (isClick) {
                if (willShowClick == null || willShowClick.provide()) asPopup().show(view1, x, y);
            }else{
                if (willShowLongClick == null || willShowLongClick.provide())  asPopup().show(view1, x, y);
            }
        });
        return this;
    }

    public SWidget asScreen() {
        SWidget screen = new SWidget(this);
        this.viewWrapper = screen;
        return screen;
    }

    public SWidget asScreen(NavigationAction action) {
        SWidget screen = asScreen();
        Navigator.action(action, screen);
        return screen;
    }

    public CardWidget asCard() {
        CardWidget card = new CardWidget(this);
        this.viewWrapper = card;
        return card;
    }


}
