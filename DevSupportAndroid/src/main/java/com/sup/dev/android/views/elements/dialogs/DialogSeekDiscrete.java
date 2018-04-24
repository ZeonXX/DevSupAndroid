package com.sup.dev.android.views.elements.dialogs;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsResources;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.providers.Provider1;

public class DialogSeekDiscrete extends BaseDialog implements SeekBar.OnSeekBarChangeListener {

    private final UtilsResources utilsResources = SupAndroid.di.utilsResources();

    private final SeekBar vSeekBar;
    private final TextView vMin;
    private final TextView vCurrent;
    private final TextView vMax;

    private Provider1<Integer, String> currentTextMask;

    public DialogSeekDiscrete(Context viewContext) {
        super(viewContext, R.layout.dialog_seek_discrete);

        vSeekBar = view.findViewById(R.id.seek);
        vMin = view.findViewById(R.id.min);
        vCurrent = view.findViewById(R.id.current);
        vMax = view.findViewById(R.id.max);

        vCurrent.setTextSize(19);
        vMax.setTextSize(16);
        vMin.setTextSize(16);
        vMax.setText(null);
        vMin.setText(null);
        vCurrent.setText(null);

        vSeekBar.setOnSeekBarChangeListener(this);
    }

    public DialogSeekDiscrete setCurrentTextMask(Provider1<Integer, String> currentTextMask) {
        this.currentTextMask = currentTextMask;
        return this;
    }

    public DialogSeekDiscrete setMax(int max) {
        vSeekBar.setMax(max);
        if (vMax.getText().toString().isEmpty())
            vMax.setText(max + "");
        return this;
    }

    public DialogSeekDiscrete setMaxMask(String mask) {
        vMax.setText(mask);
        return this;
    }

    public DialogSeekDiscrete setMinMask(String mask) {
        vMin.setText(mask);
        return this;
    }

    public DialogSeekDiscrete setProgress(int progress) {
        vSeekBar.setProgress(progress);
        return this;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (currentTextMask != null)
            vCurrent.setText(currentTextMask.provide(progress));
        else
            vCurrent.setText(progress + "");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    //
    //  Setters
    //

    public DialogSeekDiscrete setTitle(@StringRes int title) {
        return (DialogSeekDiscrete) super.setTitle(title);
    }

    public DialogSeekDiscrete setTitle(String title) {
        return (DialogSeekDiscrete) super.setTitle(title);
    }

    public DialogSeekDiscrete setAutoHideOnCancel(boolean autoHideOnCancel) {
        return (DialogSeekDiscrete) super.setAutoHideOnCancel(autoHideOnCancel);
    }

    public DialogSeekDiscrete setAutoHideOnEnter(boolean autoHideOnEnter) {
        return (DialogSeekDiscrete) super.setAutoHideOnEnter(autoHideOnEnter);
    }

    public DialogSeekDiscrete setCancelable(boolean cancelable) {
        return (DialogSeekDiscrete) super.setCancelable(cancelable);
    }

    public DialogSeekDiscrete setOnCancel(String s) {
        return (DialogSeekDiscrete) super.setOnCancel(s);
    }

    public DialogSeekDiscrete setOnCancel(Callback1<BaseDialog> onCancel) {
        return (DialogSeekDiscrete) super.setOnCancel(onCancel);
    }

    public DialogSeekDiscrete setOnCancel(@StringRes int s) {
        return (DialogSeekDiscrete) super.setOnCancel(s);
    }

    public DialogSeekDiscrete setOnCancel(@StringRes int s, Callback1<BaseDialog> onCancel) {
        return (DialogSeekDiscrete) super.setOnCancel(s, onCancel);
    }

    public DialogSeekDiscrete setOnCancel(String s, Callback1<BaseDialog> onCancel) {
        return (DialogSeekDiscrete) super.setOnCancel(s, onCancel);
    }

    public DialogSeekDiscrete setOnEnter(@StringRes int s) {
        return setOnEnter(s, (Callback2<DialogSeekDiscrete, Integer>) null);
    }

    public DialogSeekDiscrete setOnEnter(String s) {
        return setOnEnter(s, (Callback2<DialogSeekDiscrete, Integer>) null);
    }

    public DialogSeekDiscrete setOnEnter(@StringRes int s, Callback2<DialogSeekDiscrete, Integer> onEnter) {
        return setOnEnter(utilsResources.getString(s), onEnter);
    }

    public DialogSeekDiscrete setOnEnter(String s, Callback2<DialogSeekDiscrete, Integer> onEnter) {
        super.setOnEnter(s, d -> {
            if (onEnter != null) onEnter.callback(this, vSeekBar.getProgress());
        });
        return this;
    }

    public DialogSeekDiscrete setEnabled(boolean enabled) {
        vSeekBar.setEnabled(enabled);
        return (DialogSeekDiscrete) super.setEnabled(enabled);
    }

}
