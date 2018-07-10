package com.sup.dev.android.views.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sup.dev.android.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.animations.AnimationFocus;
import com.sup.dev.java.classes.animation.AnimationSpringColor;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.tools.ToolsColor;

public class ViewChip extends FrameLayout {

    private final AnimationFocus animationFocus;
    private final AnimationSpringColor animationBackground;
    private final Paint paint;
    private final Path path;

    private final View view;
    private final TextView vTextView;
    private final ViewCircleImage vIcon;

    private boolean hasIcon = false;
    private boolean selectionMode = false;
    private boolean canUnselect = true;
    private boolean canSelect = true;
    private boolean useIconBackground = false;
    private int background;
    private int unselectedBackground;

    private boolean isChipSelected = true;

    public Callback1<Boolean> onActiveChange;

    public ViewChip(@NonNull Context context) {
        this(context, null);
    }

    public ViewChip(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        SupAndroid.initEditMode(this);
        int focusColor = ToolsResources.getColor(R.color.focus);
        unselectedBackground = focusColor;

        setWillNotDraw(false);
        background = ToolsResources.getAccentColor(context);

        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        ToolsView.inflate(context, R.layout.view_chip);

        view = ToolsView.inflate(context, R.layout.view_chip);
        vTextView = view.findViewById(R.id.dev_sup_text);
        vIcon = view.findViewById(R.id.dev_sup_icon);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewChip, 0, 0);
        String text = a.getString(R.styleable.ViewChip_android_text);
        background = a.getColor(R.styleable.ViewChip_ViewChip_background, background);
        selectionMode = a.getBoolean(R.styleable.ViewChip_ViewChip_selectionMode, selectionMode);
        canSelect = a.getBoolean(R.styleable.ViewChip_ViewChip_canSelect, canSelect);
        canUnselect = a.getBoolean(R.styleable.ViewChip_ViewChip_canUnselect, canUnselect);
        unselectedBackground = a.getColor(R.styleable.ViewChip_ViewChip_unselectBackground, unselectedBackground);
        isChipSelected = a.getBoolean(R.styleable.ViewChip_ViewChip_selected, isChipSelected);
        useIconBackground = a.getBoolean(R.styleable.ViewChip_ViewChip_iconUseBackground, useIconBackground);
        int textColor = a.getColor(R.styleable.ViewChip_ViewChip_textColor, 0);
        int textStyle = a.getInteger(R.styleable.ViewChip_ViewChip_textStyle, 0);
        int icon = a.getResourceId(R.styleable.ViewChip_ViewChip_icon, 0);
        float iconPadding = a.getDimension(R.styleable.ViewChip_ViewChip_iconPadding, 0);
        float size = a.getDimension(R.styleable.ViewChip_ViewChip_size, ToolsView.dpToPx(28));
        a.recycle();

        animationFocus = new AnimationFocus(this, focusColor);
        animationBackground = new AnimationSpringColor(isChipSelected ? background : unselectedBackground, 200);

        vTextView.setText(text);
        addView(view);

        setSize(ToolsView.pxToDp(size));
        setIconPadding(ToolsView.pxToDp(iconPadding));
        setIcon(icon);
        setSelectionMode(selectionMode);
        if(textColor != 0) vTextView.setTextColor(textColor);
        else if(textStyle == 1) vTextView.setTypeface(vTextView.getTypeface(), Typeface.BOLD);
        else if(textStyle == 2) vTextView.setTypeface(vTextView.getTypeface(), Typeface.ITALIC);
        else if(textStyle == 3) vTextView.setTypeface(vTextView.getTypeface(), Typeface.BOLD_ITALIC);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        animationBackground.update();

        paint.setColor(animationBackground.getColor());
        canvas.drawPath(path, paint);

        super.onDraw(canvas);

        paint.setColor(animationFocus.update());
        canvas.drawPath(path, paint);

        if (animationBackground.isNeedUpdate()) invalidate();
    }


    public void performUserClick() {
        if (!selectionMode) return;
        if (isChipSelected() && !canUnselect) return;
        if (!isChipSelected() && !canSelect) return;

        setChipSelectedAnimated(!isChipSelected());

        if (onActiveChange != null) onActiveChange.callback(isChipSelected());
    }

    //
    //  Chip
    //


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        recreateChip();
    }

    private void recreateChip() {

        path.reset();

        if (getHeight() < getWidth()) {
            if (!hasIcon || useIconBackground || vIcon.isDisableCircle())
                path.addArc(new RectF(0, 0, getHeight(), getHeight()), 90, 180);
            path.addArc(new RectF(getWidth() - getHeight(), 0, getWidth(), getHeight()), 270, 180);
            path.addRect(getHeight() / 2, 0, getWidth() - getHeight() / 2, getHeight(), Path.Direction.CCW);
        } else {
            if (!hasIcon || useIconBackground || vIcon.isDisableCircle())
                path.addCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, Path.Direction.CCW);
        }
    }

    private void update() {
        ((FrameLayout.LayoutParams) vTextView.getLayoutParams()).setMargins(hasIcon ? vIcon.getLayoutParams().width / 3 * 2 : 0, 0, 0, 0);
        vIcon.setVisibility(hasIcon ? VISIBLE : GONE);
        vTextView.setVisibility(vTextView.getText().length() == 0 ? GONE : VISIBLE);
        setVisibility(vIcon.getVisibility() == VISIBLE || vTextView.getVisibility() == VISIBLE ? VISIBLE : GONE);
        requestLayout();
    }

    private void updateColors(boolean animated) {
        int bcColor = isChipSelected ? background : unselectedBackground;
        if (!isEnabled()) bcColor = ToolsColor.setAlpha(ToolsColor.alpha(bcColor) - 80, bcColor);
        animationBackground.change(animated, bcColor);
        animationFocus.setClickAnimationEnabled(!isChipSelected);
        invalidate();
    }

    //
    //  Setters
    //


    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        vTextView.setEnabled(enabled);
        vIcon.setEnabled(enabled);
        updateColors(false);
    }

    public void setEnabledAnimated(boolean enabled) {
        super.setEnabled(enabled);
        vTextView.setEnabled(enabled);
        vIcon.setEnabled(enabled);
        updateColors(true);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
        setClickable(l != null);
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
        if (l == null) animationFocus.resetTouchListener();
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        super.setOnFocusChangeListener(l);
        if (l == null) animationFocus.resetOnFocusChangedListener();
    }

    public void setIconPadding(int dp) {
        int px = ToolsView.dpToPx(dp);
        vIcon.setDisableCircle(px > 0);
        vIcon.setPadding(px, px, px, px);
        recreateChip();
    }

    public void setIcon(@DrawableRes int icon) {
        hasIcon = icon != 0;

        if (icon == 0) vIcon.setImageBitmap(null);
        else vIcon.setImageResource(icon);
        update();

    }

    public void setIcon(Bitmap bitmap) {
        hasIcon = bitmap != null;

        vIcon.setImageBitmap(bitmap);
        update();
    }

    public void setSize(int dp) {
        int size = ToolsView.dpToPx(dp);
        vIcon.getLayoutParams().width = size;
        vIcon.getLayoutParams().height = size;
        vTextView.getLayoutParams().height = size;
        vTextView.setTextSize(dp / 2.5f);
        vTextView.setPadding(size / 3, 0, size / 3, 0);
    }

    public void setChipSelected(boolean b) {
        isChipSelected = b;
        updateColors(false);
    }

    public void setChipSelectedAnimated(boolean b) {
        isChipSelected = b;
        updateColors(true);
    }

    public void setText(@StringRes int text) {
        vTextView.setText(text);
        update();
    }

    public void setText(String text) {
        vTextView.setText(text);
        update();
    }

    public void setCanSelect(boolean canSelect) {
        this.canSelect = canSelect;
    }

    public void setCanUnselect(boolean canUnselect) {
        this.canUnselect = canUnselect;
    }

    public void setOnActiveChange(Callback1<Boolean> onActiveChange) {
        this.onActiveChange = onActiveChange;
    }

    public void setSelectionMode(boolean selectionMode) {
        this.selectionMode = selectionMode;
        setOnClickListener(selectionMode ? vi -> performUserClick() : null);
    }

    public void setChipBackgroundRes(@ColorRes int background) {
        setChipBackground(ToolsResources.getColor(background));
    }

    public void setChipBackground(@ColorInt int background) {
        this.background = background;
        updateColors(false);
    }

    public void setChipBackgroundAnimated(@ColorInt int background) {
        this.background = background;
        updateColors(true);
    }

    public void setUnselectedBackground(@ColorInt int unselectedBackground) {
        this.unselectedBackground = unselectedBackground;
        updateColors(false);
    }

    public void setUnselectedBackgroundAnimated(@ColorInt int unselectedBackground) {
        this.unselectedBackground = unselectedBackground;
        updateColors(true);
    }

    public void setUseIconBackground(boolean useIconBackground) {
        this.useIconBackground = useIconBackground;
        recreateChip();
        invalidate();
    }

    //
    //  Getters
    //

    public boolean isChipSelected() {
        return isChipSelected;
    }

    public String getText() {
        return vTextView.getText().toString();
    }

    public boolean hasIcon() {
        return hasIcon;
    }

    //
    //  Static instance
    //

    public static ViewChip[] instanceSelectionRadio(Context viewContext, String[] texts, Callback1<String> onSelected) {
        return instanceSelectionRadio(viewContext, texts, texts, onSelected);
    }

    public static <K> ViewChip[] instanceSelectionRadio(Context viewContext, String[] texts, K[] tags, Callback1<K> onSelected) {

        if (texts.length != tags.length)
            throw new IllegalArgumentException("Texts and Tags lengths must be equals");

        ViewChip[] views = new ViewChip[texts.length];
        for (int i = 0; i < texts.length; i++) {
            int n = i;
            views[i] = instanceSelection(viewContext, texts[i], b -> {
                for (ViewChip v : views) if (v != views[n]) v.setChipSelectedAnimated(false);
                onSelected.callback(tags[n]);
            });
            views[i].setCanUnselect(false);
            views[i].setChipSelected(i == 0);
        }
        return views;
    }

    public static <K> ViewChip instanceSelection(Context viewContext, String text, K tag, Callback2<Boolean, K> onSelectionChanged) {
        return instanceSelection(viewContext, text, b -> onSelectionChanged.callback(b, tag));
    }

    public static ViewChip instanceSelection(Context viewContext, String text, Callback1<Boolean> onSelectionChanged) {
        ViewChip v = new ViewChip(viewContext);
        v.setSelectionMode(true);
        v.setText(text);
        v.setOnActiveChange(onSelectionChanged);
        return v;
    }


}
