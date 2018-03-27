package com.sup.dev.android.libs.mvp.activity_navigation;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.implementations.UtilsTextImpl;
import com.sup.dev.android.utils.interfaces.UtilsResources;
import com.sup.dev.android.utils.interfaces.UtilsView;
import com.sup.dev.java.classes.callbacks.simple.Callback;

public class DrawerMenuItem {

    private final UtilsView utilsView = SupAndroid.di.utilsView();
    private final UtilsResources utilsResources = SupAndroid.di.utilsResources();
    private final View view;
    private final TextView vText;
    private final TextView vCounter;


    public DrawerMenuItem(Context viewContext) {
        view = utilsView.inflate(viewContext, R.layout.mvp_drawer_menu_row);
        vText = view.findViewById(R.id.text);
        vCounter = view.findViewById(R.id.counter);
        vText.setText(null);
        vCounter.setVisibility(View.INVISIBLE);
    }

    public DrawerMenuItem setText(@StringRes int name){
       return setText(utilsResources.getString(name));
    }

    public DrawerMenuItem setText(String name){
        vText.setText(name);
        return this;
    }

    public DrawerMenuItem setIcon(@DrawableRes int icon){
        vText.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
        return this;
    }

    public DrawerMenuItem setOnClick(Callback onClick){
        vText.setOnClickListener(v -> onClick.callback());
        return this;
    }

    public DrawerMenuItem setCounter(int counter){
        if(counter < 1)
            utilsView.toAlpha(vCounter);
        else{
            vCounter.setText(UtilsTextImpl.numToStringK(counter));
            utilsView.fromAlpha(vCounter);
        }
        return this;
    }

    public DrawerMenuItem marginTop(int dp){
        ((ViewGroup.MarginLayoutParams)vText.getLayoutParams()).topMargin = utilsView.dpToPx(dp);
        vText.requestLayout();
        return this;
    }

    public View getView(){
        return view;
    }

}
