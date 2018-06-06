package com.sup.dev.android.views.popups;


import android.content.Context;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;

public class PopupAlert extends BasePopup {

    private TextView vTitle;
    private TextView vText;

    public PopupAlert(View anchor) {
        super(anchor, R.layout.popup_alert);
    }

    public PopupAlert(Context viewContext) {
        super(viewContext, R.layout.popup_alert);
    }

    @Override
    protected void init() {
        vTitle = findViewById(R.id.title);
        vText = findViewById(R.id.text);

        vText.setText("");
        vTitle.setVisibility(View.GONE);
    }

    public PopupAlert setText(@StringRes int text) {
        return setText(ToolsResources.getString(text));
    }

    public PopupAlert setText(String text) {
        vText.setText(text);
        return this;
    }

    public PopupAlert setTitle(@StringRes int title) {
        return setTitle(ToolsResources.getString(title));
    }

    public PopupAlert setTitle(String title) {
        vTitle.setText(title);
        vTitle.setVisibility(title == null ? View.GONE : View.VISIBLE);
        return this;
    }


}