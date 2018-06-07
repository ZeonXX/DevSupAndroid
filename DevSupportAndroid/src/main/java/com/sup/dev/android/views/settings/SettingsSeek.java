package com.sup.dev.android.views.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.views.widgets.ViewIcon;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

public class SettingsSeek extends Settings implements SeekBar.OnSeekBarChangeListener {

    private final ViewIcon vIcon;
    private final TextView vTitle;
    private final TextView vSubtitle;
    private final SeekBar vSeekBar;

    private Callback1<Integer> onProgressChanged;

    public SettingsSeek(@NonNull Context context) {
        this(context, null);
    }

    public SettingsSeek(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, R.layout.settings_seek);

        vIcon = view.findViewById(R.id.dev_sup_icon);
        vTitle = view.findViewById(R.id.dev_sup_title);
        vSubtitle = view.findViewById(R.id.dev_sup_subtitle);
        vSeekBar = view.findViewById(R.id.dev_sup_seek_bar);

        vSeekBar.setId(View.NO_ID);  //   Чтоб система не востонавливала состояние

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SettingsSeek, 0, 0);
        boolean lineVisible = a.getBoolean(R.styleable.SettingsSeek_SettingsSeek_lineVisible, true);
        String title = a.getString(R.styleable.SettingsSeek_SettingsSeek_title);
        String subtitle = a.getString(R.styleable.SettingsSeek_SettingsSeek_subtitle);
        int icon = a.getResourceId(R.styleable.SettingsSeek_SettingsSeek_icon, 0);
        int maxProgress = a.getInteger(R.styleable.SettingsSeek_SettingsSeek_maxProgress, 100);
        int progress = a.getInteger(R.styleable.SettingsSeek_SettingsSeek_progress, 70);
        int dpadStep = a.getInteger(R.styleable.SettingsSeek_SettingsSeek_dpad_step, 1);
        int iconBackground = a.getResourceId(R.styleable.SettingsAction_SettingsAction_icon_background, 0x01FF0000);
        a.recycle();

        vSeekBar.setOnSeekBarChangeListener(this);

        setLineVisible(lineVisible);
        setTitle(title);
        setSubtitle(subtitle);
        setIcon(icon);
        setMaxProgress(maxProgress);
        setProgress(progress);
        setDpadListener(this);
        setDpadStep(dpadStep);
        setIconBackground(iconBackground);
    }

    //
    //  State
    //

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("SUPER_STATE", super.onSaveInstanceState());
        bundle.putInt("progress", vSeekBar.getProgress());
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            setProgress(bundle.getInt("progress"));
            state = bundle.getParcelable("SUPER_STATE");
        }
        super.onRestoreInstanceState(state);
    }

    //
    //  Setters
    //

    public void setTitle(@StringRes int titleRes) {
        setTitle(getContext().getString(titleRes));
    }

    public void setTitle(String title) {
        vTitle.setText(title);
        vTitle.setVisibility(title != null && !title.isEmpty() ? View.VISIBLE : GONE);
    }

    public void setSubtitle(@StringRes int subtitleRes) {
        setSubtitle(getContext().getString(subtitleRes));
    }

    public void setSubtitle(String subtitle) {
        vSubtitle.setText(subtitle);
        vSubtitle.setVisibility(subtitle != null && !subtitle.isEmpty() ? View.VISIBLE : GONE);
    }

    public void setIcon(@DrawableRes int icon) {
        if (icon == 0) vIcon.setImageBitmap(null);
        else vIcon.setImageResource(icon);
        vIcon.setVisibility(icon == 0 ? View.GONE : View.VISIBLE);
    }

    public void setIconBackground(int color) {
        vIcon.setIconBackgroundColor(color);
    }

    public void setMaxProgress(int max) {

        vSeekBar.setMax(max);
    }

    public void setProgress(int progress) {
        vSeekBar.setProgress(progress);
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        vSeekBar.setEnabled(enabled);
        vTitle.setEnabled(enabled);
        vSubtitle.setEnabled(enabled);
    }

    public void setOnProgressChanged(Callback1<Integer> onProgressChanged) {
        this.onProgressChanged = onProgressChanged;
    }

    //
    //  Getters
    //

    public int getProgress() {
        return vSeekBar.getProgress();
    }

    //
    //  Events
    //

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (onProgressChanged != null)
            onProgressChanged.callback(getProgress());
    }


    //
    //  Dpad
    //

    private int dpadStep = 1;

    public void setDpadStep(int dpadStep) {
        this.dpadStep = dpadStep;
    }

    private static void setDpadListener(final SettingsSeek settingsSeek) {

        settingsSeek.vSeekBar.setOnKeyListener((v, keyCode, event) -> {

            SeekBar seekBar = settingsSeek.vSeekBar;
            int step = settingsSeek.dpadStep;

            if (event.getAction() == KeyEvent.ACTION_DOWN &&
                    (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)) {

                int progress = seekBar.getProgress();
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    progress -= step;
                    progress = progress < 0 ? 0 : progress;
                } else {
                    progress += step;
                    progress = progress > seekBar.getMax() ? seekBar.getMax() : progress;
                }
                seekBar.setProgress(progress);
                settingsSeek.onProgressChanged(seekBar, progress, true);
                return true;
            } else {
                return false;
            }
        });
    }


}
