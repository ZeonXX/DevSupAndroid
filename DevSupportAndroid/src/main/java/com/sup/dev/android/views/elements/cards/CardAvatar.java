package com.sup.dev.android.views.elements.cards;

import android.graphics.Bitmap;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.views.widgets.ViewAvatarTitle;
import com.sup.dev.java.classes.callbacks.simple.Callback;

public class CardAvatar extends Card {

    private Callback onClick;
    private boolean dividerVisible = false;
    private boolean enabled = true;
    private int background = 0x01FF0000;
    private String title;
    private String subtitle;
    private String chipText;
    private Bitmap image;
    private @DrawableRes int chipIcon;
    private int chipIconPaddingDp;
    private @ColorInt int chipBackground;
    private boolean chipUseIconBackground;
    private Callback onClickAvatar;

    @Override
    public int getLayout() {
        return R.layout.card_avatar;
    }

    @Override
    public final void bindView(View view) {
        View vTouch = view.findViewById(R.id.touch);
        View vDivider = view.findViewById(R.id.divider);
        ViewAvatarTitle vAvatar = view.findViewById(R.id.avatar);

        vDivider.setVisibility(dividerVisible ? View.VISIBLE : View.GONE);
        vTouch.setFocusable(onClick != null && enabled);
        vTouch.setClickable(onClick != null && enabled);
        vTouch.setEnabled(onClick != null && enabled);
        vTouch.setOnClickListener((onClick != null && enabled)?v -> onClick.callback():null);
        if (background != 0x01FF0000) view.setBackgroundColor(background);
        vAvatar.setEnabled(isEnabled());
        vAvatar.setTitle(title);
        vAvatar.setSubtitle(subtitle);
        vAvatar.setClickable(false);
        vAvatar.getViewAvatar().setOnClickListener(v -> onClickAvatar.callback());
        vAvatar.getViewAvatar().setImage(image);
        vAvatar.getViewAvatar().getChip().setText(chipText);
        vAvatar.getViewAvatar().getChip().setIcon(chipIcon);
        vAvatar.getViewAvatar().getChip().setIconPadding(chipIconPaddingDp);
        vAvatar.getViewAvatar().getChip().setChipBackground(chipBackground);
        vAvatar.getViewAvatar().getChip().setUseIconBackground(chipUseIconBackground);

        onBind(vAvatar);
    }

    protected void onBind(ViewAvatarTitle vAvatar) {

    }

    //
    //  Setters
    //

    public CardAvatar setOnCLickAvatar(Callback onClickAvatar){
       this.onClickAvatar = onClickAvatar;
        update();
        return this;
    }

    public CardAvatar setTitle(String title) {
        this.title = title;
        update();
        return this;
    }

    public CardAvatar setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        update();
        return this;
    }

    public CardAvatar setImage(Bitmap image) {
        this.image = image;
        update();
        return this;
    }

    public CardAvatar setChipIcon(@DrawableRes int icon) {
        this.chipIcon = icon;
        update();
        return this;
    }

    public CardAvatar setChipIconPadding(int dp) {
        chipIconPaddingDp = dp;
        update();
        return this;
    }

    public CardAvatar setChipBackground(@ColorInt int chipBackground) {
        this.chipBackground = chipBackground;
        update();
        return this;
    }

    public CardAvatar setChipText(String avatarText) {
        this.chipText = avatarText;
        update();
        return this;
    }

    public CardAvatar setEnabled(boolean enabled) {
        this.enabled = enabled;
        update();
        return this;
    }

    public CardAvatar setOnClick(Callback onClick) {
        this.onClick = onClick;
        update();
        return this;
    }

    public CardAvatar setDividerVisible(boolean dividerVisible) {
        this.dividerVisible = dividerVisible;
        update();
        return this;
    }

    public CardAvatar setBackground(int background) {
        this.background = background;
        update();
        return this;
    }

    public CardAvatar setChipUseIconBackground(boolean chipUseIconBackground) {
        this.chipUseIconBackground = chipUseIconBackground;
        update();
        return this;
    }

    //
    //  Getters
    //

    public boolean isEnabled() {
        return enabled;
    }

}
