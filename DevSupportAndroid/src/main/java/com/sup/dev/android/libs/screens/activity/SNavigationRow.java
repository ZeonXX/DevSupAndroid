package com.sup.dev.android.libs.screens.activity;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sup.dev.android.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.views.ViewChip;

public class SNavigationRow {

    private final View view;
    private final ViewChip vChip;

    public SNavigationRow(Context context, @DrawableRes int icon, @StringRes int text, View.OnClickListener onClickListener){
        this(context, icon, ToolsResources.getString(text), onClickListener);
    }

    public SNavigationRow(Context context, @DrawableRes int icon, String text, View.OnClickListener onClickListener){
        view = ToolsView.inflate(context, R.layout.screen_activity_navigation_row);
        vChip = view.findViewById(R.id.navigation_row_chip);

        ((ImageView)view.findViewById(R.id.navigation_row_icon)).setImageResource(icon);
        view.setOnClickListener(onClickListener);

        setChipVisible(false);
        ((TextView)view.findViewById(R.id.navigation_row_text)).setText(text);
    }

    public void setChip(String text){
        vChip.setText(text);
    }

    public void setChipVisible(boolean b){
        view.findViewById(R.id.navigation_row_chip).setVisibility(b?View.VISIBLE:View.GONE);
    }

    public View getView() {
        return view;
    }
}
