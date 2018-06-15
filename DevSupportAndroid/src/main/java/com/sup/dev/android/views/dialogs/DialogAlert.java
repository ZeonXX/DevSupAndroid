package com.sup.dev.android.views.dialogs;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsStorage;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

public class DialogAlert extends BaseDialog {

    private String key;
    private boolean lockUntilAccept;

    private final ViewGroup vImageContainer;
    private final ImageView vImage;
    private final CheckBox vCheck;

    public static boolean check(String key) {
        return ToolsStorage.getBoolean(key, false);
    }

    public static void clear(String key) {
        ToolsStorage.remove(key);
    }

    public DialogAlert(Context viewContext) {
        super(viewContext, R.layout.dialog_alert);

        vImageContainer = view.findViewById(R.id.dev_sup_dialog_image_container);
        vImage = view.findViewById(R.id.dev_sup_dialog_image);
        vCheck = view.findViewById(R.id.check_box);

        vImageContainer.setVisibility(View.GONE);
        vImage.setVisibility(View.GONE);
        vCheck.setVisibility(View.GONE);

        vCheck.setOnCheckedChangeListener((compoundButton, b) -> updateLock());
    }

    private void updateLock(){
        if(lockUntilAccept)
            vEnter.setEnabled(vCheck.isChecked());
    }

    //
    //  Setters
    //

    public DialogAlert setLockUntilAccept(boolean lockUntilAccept) {
        this.lockUntilAccept = lockUntilAccept;
        updateLock();
        return this;
    }

    public DialogAlert setChecker(String key, @StringRes int text) {
        return setChecker(key, ToolsResources.getString(text));
    }

    public DialogAlert setChecker(String key, String text) {
        this.key = key;
        vCheck.setText(text);
        vCheck.setVisibility(text == null || key == null ? View.GONE : View.VISIBLE);
        return this;
    }

    public DialogAlert setTitleViewBackgroundR(@ColorRes int res) {
        return setTitleViewBackground(ToolsResources.getColor(res));
    }

    public DialogAlert setTitleViewBackground(int color) {
        vImageContainer.setBackgroundColor(color);
        vImageContainer.setVisibility(View.VISIBLE);
        return this;
    }

    public DialogAlert setTitleImage(@DrawableRes int res) {
        vImage.setImageResource(res);
        vImageContainer.setVisibility(View.VISIBLE);
        vImage.setVisibility(View.VISIBLE);
        return this;
    }

    public DialogAlert setTitleImage(Bitmap bitmap) {
        vImage.setImageBitmap(bitmap);
        vImageContainer.setVisibility(View.VISIBLE);
        vImage.setVisibility(View.VISIBLE);
        return this;
    }

    public DialogAlert setTitleView(View view) {
        vImageContainer.removeAllViews();
        vImageContainer.addView(view);
        vImageContainer.setVisibility(View.VISIBLE);
        return this;
    }

    public DialogAlert setTitleView(@LayoutRes int res) {
        vImageContainer.removeAllViews();
        vImageContainer.addView(ToolsView.inflate(viewContext, res));
        vImageContainer.setVisibility(View.VISIBLE);
        return this;
    }

    public DialogAlert addLine(String text) {
        CharSequence s = vText.getText();
        vText.setVisibility(View.VISIBLE);
        if (s.length() == 0)
            vText.setText(text);
        else
            vText.setText(s + "\n" + text);
        return this;
    }


    public DialogAlert setTitle(@StringRes int title) {
        return (DialogAlert) super.setTitle(title);
    }

    public DialogAlert setTitle(String title) {
        return (DialogAlert) super.setTitle(title);
    }

    public DialogAlert setText(@StringRes int text) {
        return (DialogAlert) super.setText(text);
    }

    public DialogAlert setText(CharSequence text) {
        return (DialogAlert)super.setText(text);
    }

    public DialogAlert setAutoHideOnCancel(boolean autoHideOnCancel) {
        return (DialogAlert) super.setAutoHideOnCancel(autoHideOnCancel);
    }

    public DialogAlert setCancelable(boolean cancelable) {
        return (DialogAlert) super.setCancelable(cancelable);
    }

    public DialogAlert setOnEnter(@StringRes int s) {
        return (DialogAlert) super.setOnEnter(s);
    }

    public DialogAlert setOnEnter(String s) {
        return (DialogAlert) super.setOnEnter(s);
    }

    public DialogAlert setOnEnter(@StringRes int s, Callback1<BaseDialog> onEnter) {
        return (DialogAlert) super.setOnEnter(s, onEnter);
    }

    public DialogAlert setOnEnter(String s, Callback1<BaseDialog> onEnter) {
        super.setOnEnter(s, key == null ? onEnter : d -> {
            ToolsStorage.put(key, vCheck.isChecked());
            if (onEnter != null) onEnter.callback(this);
        });
        return this;
    }

    public DialogAlert setAutoHideOnEnter(boolean autoHideOnEnter) {
        return (DialogAlert) super.setAutoHideOnEnter(autoHideOnEnter);
    }

    public DialogAlert setOnCancel(String s) {
        return (DialogAlert) super.setOnCancel(s);
    }

    public DialogAlert setOnCancel(Callback1<BaseDialog> onCancel) {
        return (DialogAlert) super.setOnCancel(onCancel);
    }

    public DialogAlert setOnCancel(@StringRes int s) {
        return (DialogAlert) super.setOnCancel(s);
    }

    public DialogAlert setOnCancel(@StringRes int s, Callback1<BaseDialog> onCancel) {
        return (DialogAlert) super.setOnCancel(s, onCancel);
    }

    public DialogAlert setOnCancel(String s, Callback1<BaseDialog> onCancel) {
        return (DialogAlert) super.setOnCancel(s, onCancel);
    }

    public DialogAlert setEnabled(boolean enabled) {
        vCheck.setEnabled(enabled);
        return (DialogAlert) super.setEnabled(enabled);
    }


}
