package com.sup.dev.android.views.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsBitmap;

public class SettingsArrow extends SettingsAction {

    private final ImageView vArrow;

    public SettingsArrow(@NonNull Context context) {
        this(context, null);
    }

    public SettingsArrow(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        vArrow = new ImageView(context);
        vArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SettingsArrow, 0, 0);
        boolean lineVisible = a.getBoolean(R.styleable.SettingsArrow_SettingsArrow_lineVisible, true);
        String title = a.getString(R.styleable.SettingsArrow_SettingsArrow_title);
        String subtitle = a.getString(R.styleable.SettingsArrow_SettingsArrow_subtitle);
        int icon = a.getResourceId(R.styleable.SettingsArrow_SettingsArrow_icon, 0);
        int iconBackground = a.getColor(R.styleable.SettingsArrow_SettingsArrow_icon_background, 0);
        setIconBackground(iconBackground);
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
            vArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
        else
            vArrow.setImageBitmap(ToolsBitmap.filter(R.drawable.ic_keyboard_arrow_right_black_24dp, 0xAFFFFFFF, true));
    }

}