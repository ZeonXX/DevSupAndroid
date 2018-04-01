package com.sup.dev.android.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsResources;
import com.sup.dev.android.utils.interfaces.UtilsView;
import com.sup.dev.android.views.animations.AnimationFocus;

public class ViewAvatar extends FrameLayout {

    private final UtilsView utilsView;
    private final UtilsResources utilsResources;

    private final Paint paint;
    private final AnimationFocus animationFocus;

    private final ImageView vImage;
    private final ViewChip vChip;
    private final ViewDraw vTouch;

    public ViewAvatar(Context context) {
        this(context, null);
    }

    public ViewAvatar(Context context, AttributeSet attrs) {
        super(context, attrs);

        SupAndroid.initEditMode(this);
        utilsView = SupAndroid.di.utilsView();
        utilsResources = SupAndroid.di.utilsResources();
        int focusColor = utilsResources.getColor(R.color.focus);

        paint = new Paint();
        paint.setAntiAlias(true);

        View view = utilsView.inflate(context, R.layout.view_avatar);
        vImage = view.findViewById(R.id.dev_sup_image);
        vChip = view.findViewById(R.id.dev_sup_chip);
        vTouch = view.findViewById(R.id.dev_sup_avatar_touch);

        vChip.setVisibility(GONE);

        setEnabled(false);

        addView(view);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewAvatar, 0, 0);
        int src = a.getResourceId(R.styleable.ViewAvatar_android_src, R.color.blue_700);
        String text = a.getString(R.styleable.ViewAvatar_ViewAvatar_chipText);
        int chipBackground = a.getColor(R.styleable.ViewAvatar_ViewAvatar_chipBackground, 0x01FF0000);
        int srcIcon = a.getResourceId(R.styleable.ViewAvatar_ViewAvatar_chipIcon, 0);
        boolean iconUseBackground = a.getBoolean(R.styleable.ViewAvatar_ViewAvatar_chipIconUseBackground, false);
        float iconPadding = a.getDimension(R.styleable.ViewAvatar_ViewAvatar_chipIconPadding, 0);
        float chipSize = a.getDimension(R.styleable.ViewAvatar_ViewAvatar_chipSize, utilsView.dpToPx(24));
        a.recycle();

        animationFocus = new AnimationFocus(vTouch, focusColor);

        setImage(src);
        vChip.setSize(utilsView.pxToDp(chipSize));
        vChip.setIconPadding(utilsView.pxToDp(iconPadding));
        vChip.setIcon(srcIcon);
        vChip.setText(text);
        vChip.setUseIconBackground(iconUseBackground);
        if (chipBackground != 0x01FF0000) vChip.setChipBackground(chipBackground);

        vTouch.setOnDraw(canvas -> {
            paint.setColor(animationFocus.update());
            canvas.drawCircle(vTouch.getWidth()/2, vTouch.getHeight()/2, vTouch.getHeight()/2, paint);
        });
    }

    private void updateChipVisible() {

        if (!vChip.hasIcon() && vChip.getText().isEmpty())
            vChip.setVisibility(GONE);
        else
            vChip.setVisibility(VISIBLE);
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        if (params.width == LayoutParams.WRAP_CONTENT && params.height == LayoutParams.WRAP_CONTENT) {
            params.width = utilsView.dpToPx(52);
            params.height = utilsView.dpToPx(52);
        }

        if (params.width > 0 && params.height == LayoutParams.WRAP_CONTENT)
            params.height = params.width;
        if (params.height > 0 && params.width == LayoutParams.WRAP_CONTENT) params.width = params.height;

        super.setLayoutParams(params);

    }

    //
    //  Setters
    //


    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        vTouch.setOnClickListener(l);
        vTouch.setClickable( l != null);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        vImage.setEnabled(enabled);
    }

    @Override
    public void setClickable(boolean clickable) {
        super.setClickable(clickable);
        vImage.setClickable(clickable);
    }

    public void setImage(@DrawableRes int image) {
        if (image != 0) vImage.setImageResource(image);
        else vImage.setImageBitmap(null);

    }

    public void setImage(Bitmap bitmap) {
        vImage.setImageBitmap(bitmap);
    }

    //
    //  Getters
    //

    public String getText() {
        return vChip.getText();
    }

    public ImageView getImageView() {
        return vImage;
    }

    public ViewChip getChip() {
        return vChip;
    }
}
