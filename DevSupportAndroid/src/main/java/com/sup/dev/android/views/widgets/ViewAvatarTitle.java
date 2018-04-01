package com.sup.dev.android.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsResources;
import com.sup.dev.android.utils.interfaces.UtilsView;
import com.sup.dev.android.views.animations.AnimationFocus;
import com.sup.dev.java.classes.callbacks.simple.Callback;

public class ViewAvatarTitle extends FrameLayout {

    private final UtilsView utilsView;
    private final UtilsResources utilsResources;

    private final AnimationFocus animationFocus;
    private final Paint paint;
    private final Path path;

    private final ViewAvatar vAvatar;
    private final TextView vTitle;
    private final TextView vSubtitle;

    public ViewAvatarTitle(@NonNull Context context) {
        this(context, null);
    }

    public ViewAvatarTitle(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);

        SupAndroid.initEditMode(this);
        utilsView = SupAndroid.di.utilsView();
        utilsResources = SupAndroid.di.utilsResources();

        int focusColor = utilsResources.getColor(R.color.focus);

        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);

        View view = utilsView.inflate(context, R.layout.view_avatar_title);

        vAvatar = view.findViewById(R.id.dev_sup_avatar);
        vTitle = view.findViewById(R.id.dev_sup_title);
        vSubtitle = view.findViewById(R.id.dev_sup_subtitle);

        addView(view);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewAvatarTitle, 0, 0);
        int src = a.getResourceId(R.styleable.ViewAvatarTitle_android_src, R.color.blue_700);
        int srcIcon = a.getResourceId(R.styleable.ViewAvatarTitle_ViewAvatarTitle_chipIcon, 0);
        String chipText = a.getString(R.styleable.ViewAvatarTitle_ViewAvatarTitle_chipText);
        int chipBackground = a.getColor(R.styleable.ViewAvatarTitle_ViewAvatarTitle_chipBackground, 0);
        String mText = a.getString(R.styleable.ViewAvatarTitle_ViewAvatarTitle_title);
        String mSubtitle = a.getString(R.styleable.ViewAvatarTitle_ViewAvatarTitle_subtitle);
        float iconPadding = a.getDimension(R.styleable.ViewAvatarTitle_ViewAvatarTitle_chipIconPadding, 0);
        float chipSize = a.getDimension(R.styleable.ViewAvatarTitle_ViewAvatarTitle_chipSize, utilsView.dpToPx(24));
        a.recycle();

        animationFocus = new AnimationFocus(this, focusColor);

        vAvatar.setImage(src);
        vAvatar.getChip().setSize(utilsView.pxToDp(chipSize));
        vAvatar.getChip().setIconPadding(utilsView.pxToDp(iconPadding));
        vAvatar.getChip().setIcon(srcIcon);
        vAvatar.getChip().setText(chipText);
        vAvatar.getChip().setChipBackground(chipBackground);
        setTitle(mText);
        setSubtitle(mSubtitle);

    }


    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        animationFocus.update();

        paint.setColor(animationFocus.update());
        canvas.drawPath(path, paint);
    }

    //
    //  Chip
    //

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int r = getHeight() / 2;
        path.reset();

        if (getHeight() < getWidth()) {
            path.addCircle(r, r, r, Path.Direction.CCW);
            path.addCircle(getWidth() - r, r, r, Path.Direction.CCW);
            path.addRect(r, 0, getWidth() - r, getHeight(), Path.Direction.CCW);
        } else {
            path.addCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, Path.Direction.CCW);
        }

    }

    //
    //  Setters
    //

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

    public void setTitle(@StringRes int text) {
        vTitle.setText(text);
    }

    public void setTitle(String text) {
        vTitle.setText(text);
    }

    public void setSubtitle(@StringRes int text) {
        vSubtitle.setText(text);
    }

    public void setSubtitle(String text) {
        vSubtitle.setVisibility(text == null || text.isEmpty() ? GONE : VISIBLE);
        vSubtitle.setText(text);
    }

    //
    //  Getters
    //

    public ViewAvatar getViewAvatar() {
        return vAvatar;
    }

    public String getTitle() {
        return vTitle.getText().toString();
    }

    public String getSubTitle() {
        return vSubtitle.getText().toString();
    }

}
