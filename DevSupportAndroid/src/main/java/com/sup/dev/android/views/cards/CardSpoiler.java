package com.sup.dev.android.views.cards;

import android.graphics.PorterDuff;
import android.support.annotation.StringRes;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sup.dev.android.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.views.adapters.CardAdapter;

import java.util.ArrayList;

public class CardSpoiler extends Card {

    private final ArrayList<Card> cards = new ArrayList<>();
    private String title;
    private String text;
    private String rightText;
    private int titleColor = 0;
    private int rightTextColor = 0;
    private int textColor = 0;
    private boolean originalSeted = false;
    private boolean dividerVisible = true;
    private int titleColorOriginal;
    private int rightTextColorOriginal;
    private int textColorOriginal;
    private int iconColor = 0;

    boolean expanded;
    boolean enabled = true;

    @Override
    public int getLayout() {
        return R.layout.card_spoiler;
    }

    @Override
    public void bindView(View view) {
        ImageView vIcon = view.findViewById(R.id.icon);
        TextView vTitle = view.findViewById(R.id.title);
        TextView vText = view.findViewById(R.id.text);
        TextView vRightText = view.findViewById(R.id.right_text);
        View vTouch = view.findViewById(R.id.touch);
        View vDivider = view.findViewById(R.id.divider);

        if (!originalSeted) {
            originalSeted = true;
            titleColorOriginal = vTitle.getCurrentTextColor();
            textColorOriginal = vText.getCurrentTextColor();
            rightTextColorOriginal = vRightText.getCurrentTextColor();
        }

        vText.setText(text == null ? null : Html.fromHtml(text));
        vRightText.setText(rightText == null ? null : Html.fromHtml(rightText));
        vTitle.setText(title == null ? null : Html.fromHtml(title));

        vText.setVisibility(text == null ? View.GONE : View.VISIBLE);
        vRightText.setVisibility(rightText == null ? View.GONE : View.VISIBLE);
        vTitle.setVisibility(title == null ? View.GONE : View.VISIBLE);

        vText.setEnabled(enabled);
        vRightText.setEnabled(enabled);
        vTitle.setEnabled(enabled);

        vDivider.setVisibility(dividerVisible?View.VISIBLE:View.GONE);
        vText.setTextColor(textColor != 0 ? textColor : textColorOriginal);
        vRightText.setTextColor(rightTextColor != 0 ? rightTextColor : rightTextColorOriginal);
        vTitle.setTextColor(titleColor != 0 ? titleColor : titleColorOriginal);

        vIcon.setImageResource(expanded ? R.drawable.ic_keyboard_arrow_up_black_24dp : R.drawable.ic_keyboard_arrow_down_black_24dp);
        vIcon.setAlpha(enabled ? 255 : 106);
        if (iconColor != 0) vIcon.setColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP);
        vTouch.setOnClickListener(!enabled ? null : v -> setExpanded(!expanded));
        vTouch.setClickable(enabled);
    }

    //
    //  Setters
    //

    public CardSpoiler add(Card card) {
        cards.add(card);
        setExpanded(expanded);
        return this;
    }

    public CardSpoiler setTitle(@StringRes int title) {
        return setTitle(ToolsResources.getString(title));
    }

    public CardSpoiler setTitle(String title) {
        this.title = title;
        update();
        return this;
    }

    @Override
    public void setAdapter(CardAdapter adapter) {
        super.setAdapter(adapter);
        setExpanded(expanded);
    }

    public CardSpoiler setExpanded(boolean expanded) {
        this.expanded = expanded;
        update();

        if(adapter != null) {
            if (expanded) {

                int myIndex = adapter.indexOf(this);
                for (Card c : cards)
                    if (myIndex != -1)
                        if (!adapter.contains(c)) adapter.add(++myIndex, c);

            } else
                for (Card c : cards) adapter.remove(c);
        }

        return this;
    }

    public CardSpoiler setEnabled(boolean enabled) {
        this.enabled = enabled;
        update();
        return this;
    }

    public CardSpoiler setText(String text) {
        this.text = text;
        update();
        return this;
    }

    public CardSpoiler setRightText(String rightText) {
        this.rightText = rightText;
        update();
        return this;
    }

    public CardSpoiler setIconColor(int iconColor) {
        this.iconColor = iconColor;
        update();
        return this;
    }

    public CardSpoiler setTextColor(int textColor) {
        this.textColor = textColor;
        update();
        return this;
    }

    public CardSpoiler setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        update();
        return this;
    }

    public CardSpoiler setRightTextColor(int rightTextColor) {
        this.rightTextColor = rightTextColor;
        update();
        return this;
    }

    public CardSpoiler setDividerVisible(boolean dividerVisible) {
        this.dividerVisible = dividerVisible;
        update();
        return this;
    }

    //
    //  Getters
    //

    public ArrayList<Card> getCards() {
        return cards;
    }
}
