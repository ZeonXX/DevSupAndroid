package com.sup.dev.android.views.bricks;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
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
import com.sup.dev.android.tools.ToolsText;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

public class BrickAlert extends Brick {

    private String key;
    private String checkText;
    private String enterText;
    private String cancelText;
    private CharSequence text;
    private String title;
    private Callback1<BrickAlert> onEnter;
    private Callback1<BrickAlert> onCancel;
    private boolean lockUntilAccept;
    private boolean autoHideOnEnter = true;
    private int imageBackground;
    private int image;

    public static boolean check(String key) {
        return ToolsStorage.getBoolean(key, false);
    }

    public static void clear(String key) {
        ToolsStorage.remove(key);
    }

    @Override
    public void bindView(View view, Mode mode) {
        TextView vTitle = view.findViewById(R.id.title);
        TextView vText = view.findViewById(R.id.text);
        Button vCancel = view.findViewById(R.id.cancel);
        Button vEnter = view.findViewById(R.id.enter);
        CheckBox vCheck = view.findViewById(R.id.check_box);
        ViewGroup vImageContainer = view.findViewById(R.id.image_container);
        ImageView vImage = view.findViewById(R.id.image);

        ToolsView.setTextOrGone(vTitle, title);
        ToolsView.setTextOrGone(vText, text);
        ToolsView.setTextOrGone(vCancel, cancelText);
        ToolsView.setTextOrGone(vEnter, enterText);
        ToolsView.setTextOrGone(vCheck, checkText);
        vImage.setImageResource(image);
        vImageContainer.setBackgroundColor(imageBackground);

        vCheck.setOnCheckedChangeListener((compoundButton, b) -> updateLock(vEnter, vCheck));

        vImageContainer.setVisibility(imageBackground != 0 || image != 0 ? View.VISIBLE : View.GONE);
        vImage.setVisibility(image != 0 ? View.VISIBLE : View.GONE);

        vEnter.setOnClickListener(v -> {
            if(autoHideOnEnter) hide();
            else setEnabled(false);
            if(key != null)ToolsStorage.put(key, vCheck.isChecked());
            if (onEnter != null) onEnter.callback(this);
        });

        vCancel.setOnClickListener(v -> {
            hide();
            if(onCancel != null) onCancel.callback(this);
        });

    }

    @Override
    public int getLayoutRes(Mode mode) {
        return R.layout.brick_alert;
    }

    private void updateLock(Button vEnter, CheckBox vCheck) {
        if (lockUntilAccept) vEnter.setEnabled(vCheck.isChecked());
    }

    //
    //  Setters
    //

    public BrickAlert setLockUntilAccept(boolean lockUntilAccept) {
        this.lockUntilAccept = lockUntilAccept;
        update();
        return this;
    }

    public BrickAlert setChecker(String key, @StringRes int text) {
        return setChecker(key, ToolsResources.getString(text));
    }

    public BrickAlert setChecker(String key, String text) {
        this.key = key;
        this.checkText = text;
        update();
        return this;
    }


    public BrickAlert setImageBackgroundRes(@ColorRes int res) {
        return setImageBackground(ToolsResources.getColor(res));
    }

    public BrickAlert setImageBackground(@ColorInt int color) {
        this.imageBackground = color;
        update();
        return this;
    }

    public BrickAlert setImage(@DrawableRes int image) {
        this.image = image;
        update();
        return this;
    }

    public BrickAlert addLine(String text) {
        if (this.text == null) this.text = text;
        else this.text = this.text + "\n" + text;
        update();
        return this;
    }

    public BrickAlert setTitle(@StringRes int title) {
        return setTitle(ToolsResources.getString(title));
    }

    public BrickAlert setTitle(String title) {
        this.title = title;
        update();
        return this;
    }

    public BrickAlert setText(@StringRes int text) {
        return setText(ToolsResources.getString(text));
    }

    public BrickAlert setText(CharSequence text) {
        this.text = text;
        update();
        return this;
    }

    public BrickAlert setOnEnter(@StringRes int s) {
        return setOnEnter(ToolsResources.getString(s));
    }

    public BrickAlert setOnEnter(String s) {
        return setOnEnter(s, null);
    }

    public BrickAlert setOnEnter(@StringRes int s, Callback1<BrickAlert> onEnter) {
        return setOnEnter(ToolsResources.getString(s), onEnter);
    }

    public BrickAlert setOnEnter(String s, Callback1<BrickAlert> onEnter) {
        this.enterText = s;
        this.onEnter = onEnter;
        update();
        return this;
    }

    public BrickAlert setAutoHideOnEnter(boolean autoHideOnEnter) {
        this.autoHideOnEnter = autoHideOnEnter;
        return this;
    }

    public BrickAlert setOnCancel(@StringRes int s) {
        return setOnCancel(ToolsResources.getString(s), null);
    }

    public BrickAlert setOnCancel(String s) {
        return setOnCancel(s, null);
    }

    public BrickAlert setOnCancel(Callback1<BrickAlert> onCancel) {
        return setOnCancel(null, onCancel);
    }

    public BrickAlert setOnCancel(@StringRes int s, Callback1<BrickAlert> onCancel) {
        return setOnCancel(ToolsResources.getString(s), onCancel);
    }

    public BrickAlert setOnCancel(String s, Callback1<BrickAlert> onCancel) {
        super.setOnHide(b -> {
            if (onCancel != null) onCancel.callback(this);
        });
        this.onCancel = onCancel;
        this.cancelText = s;
        return this;
    }

    public BrickAlert setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        return this;
    }

}
