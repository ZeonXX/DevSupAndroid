package com.sup.dev.android.views.widgets.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsBitmap;

public class SettingsActionArrow extends SettingsAction {

    private final UtilsBitmap utilsBitmap = SupAndroid.di.utilsBitmap();

    private final ImageView vArrow;

    public SettingsActionArrow(@NonNull Context context) {
        this(context, null);
    }

    public SettingsActionArrow(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        vArrow = new ImageView(context);
        vArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_white_24dp);


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SettingsActionArrow, 0, 0);
        boolean lineVisible = a.getBoolean(R.styleable.SettingsActionArrow_SettingsActionArrow_lineVisible, true);
        String title = a.getString(R.styleable.SettingsActionArrow_SettingsActionArrow_title);
        String subtitle = a.getString(R.styleable.SettingsActionArrow_SettingsActionArrow_subtitle);
        int icon = a.getResourceId(R.styleable.SettingsActionArrow_SettingsActionArrow_icon, 0);
        a.recycle();

        setLineVisible(lineVisible);
        setTitle(title);
        setSubtitle(subtitle);
        setIcon(icon);

        setSubView(vArrow);
    }

    //
    //  Setters
    //

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled)
            vArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_white_24dp);
        else
            vArrow.setImageBitmap(utilsBitmap.filter(R.drawable.ic_keyboard_arrow_right_white_24dp, 0xAFFFFFFF, true));
    }

}