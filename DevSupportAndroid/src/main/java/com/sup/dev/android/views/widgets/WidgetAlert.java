package com.sup.dev.android.views.widgets;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsStorage;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.screens.SWidget;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

public class WidgetAlert extends Widget {

    private final CheckBox vCheck;
    private final Button vEnter;
    private final Button vCancel;
    private final TextView vText;
    private final TextView vTitle;
    private final ViewGroup vImageContainer;
    private final ImageView vImage;

    private String key;
    private boolean lockUntilAccept;
    private boolean autoHideOnEnter = true;

    public static boolean check(String key) {
        return ToolsStorage.getBoolean(key, false);
    }

    public static void clear(String key) {
        ToolsStorage.remove(key);
    }

    public WidgetAlert() {
        super(R.layout.widget_alert);
        vTitle = findViewById(R.id.title);
        vText = findViewById(R.id.text);
        vCancel = findViewById(R.id.cancel);
        vEnter = findViewById(R.id.enter);
        vCheck = findViewById(R.id.check_box);
        vImageContainer = findViewById(R.id.image_container);
        vImage = findViewById(R.id.image);

        vTitle.setVisibility(View.GONE);
        vText.setVisibility(View.GONE);
        vCancel.setVisibility(View.GONE);
        vEnter.setVisibility(View.GONE);
        vCheck.setVisibility(View.GONE);
        vImageContainer.setVisibility(View.GONE);

        vCheck.setOnCheckedChangeListener((compoundButton, b) -> updateLock(vEnter, vCheck));
    }

    @Override
    public void onShow() {
        super.onShow();

        ToolsView.setTextOrGone(vTitle, vTitle.getText());
        if(viewWrapper instanceof SWidget){
            vTitle.setVisibility(View.GONE);
            ((SWidget)viewWrapper).setTitle(vTitle.getText().toString());
        }

    }

    private void updateLock(Button vEnter, CheckBox vCheck) {
        if (lockUntilAccept) vEnter.setEnabled(vCheck.isChecked());
    }

    //
    //  Setters
    //

    public WidgetAlert setLockUntilAccept(boolean lockUntilAccept) {
        this.lockUntilAccept = lockUntilAccept;
        return this;
    }

    public WidgetAlert setChecker(String key, @StringRes int text) {
        return setChecker(key, ToolsResources.getString(text));
    }

    public WidgetAlert setChecker(String key, String text) {
        this.key = key;
        vCheck.setText(text);
        vCheck.setVisibility(View.VISIBLE);
        return this;
    }


    public WidgetAlert setImageBackgroundRes(@ColorRes int res) {
        return setImageBackground(ToolsResources.getColor(res));
    }

    public WidgetAlert setImageBackground(@ColorInt int color) {
        vImageContainer.setBackgroundColor(color);
        return this;
    }

    public WidgetAlert setImage(int image) {
        vImageContainer.setVisibility(image > 0 ? View.VISIBLE : View.GONE);
        if (image > 0) vImage.setImageResource(image);
        else vImage.setImageBitmap(null);

        return this;
    }

    public WidgetAlert addLine(String text) {
        vText.setText(vText.getText().toString() + "\n" + text);
        vText.setVisibility(View.VISIBLE);
        return this;
    }

    public WidgetAlert setTitle(@StringRes int title) {
        return setTitle(ToolsResources.getString(title));
    }

    public WidgetAlert setTitle(String title) {
        ToolsView.setTextOrGone(vTitle, title);
        return this;
    }

    public WidgetAlert setText(@StringRes int text) {
        return setText(ToolsResources.getString(text));
    }

    public WidgetAlert setText(CharSequence text) {
        ToolsView.setTextOrGone(vText, text);
        return this;
    }

    public WidgetAlert setOnEnter(@StringRes int s) {
        return setOnEnter(ToolsResources.getString(s));
    }

    public WidgetAlert setOnEnter(String s) {
        return setOnEnter(s, null);
    }

    public WidgetAlert setOnEnter(@StringRes int s, Callback1<WidgetAlert> onEnter) {
        return setOnEnter(ToolsResources.getString(s), onEnter);
    }

    public WidgetAlert setOnEnter(String s, Callback1<WidgetAlert> onEnter) {
        ToolsView.setTextOrGone(vEnter, s);
        vEnter.setOnClickListener(v -> {
            if (autoHideOnEnter) hide();
            else setEnabled(false);
            if (key != null) ToolsStorage.put(key, vCheck.isChecked());
            if (onEnter != null) onEnter.callback(this);
        });
        return this;
    }

    public WidgetAlert setAutoHideOnEnter(boolean autoHideOnEnter) {
        this.autoHideOnEnter = autoHideOnEnter;
        return this;
    }

    public WidgetAlert setOnCancel(@StringRes int s) {
        return setOnCancel(ToolsResources.getString(s), null);
    }

    public WidgetAlert setOnCancel(String s) {
        return setOnCancel(s, null);
    }

    public WidgetAlert setOnCancel(Callback1<WidgetAlert> onCancel) {
        return setOnCancel(null, onCancel);
    }

    public WidgetAlert setOnCancel(@StringRes int s, Callback1<WidgetAlert> onCancel) {
        return setOnCancel(ToolsResources.getString(s), onCancel);
    }

    public WidgetAlert setOnCancel(String s, Callback1<WidgetAlert> onCancel) {
        super.setOnHide(b -> {
            if (onCancel != null) onCancel.callback(this);
        });
        ToolsView.setTextOrGone(vCancel, s);
        vCancel.setOnClickListener(v -> {
            hide();
            if (onCancel != null) onCancel.callback(this);
        });

        return this;
    }

    public WidgetAlert setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        return this;
    }

}
