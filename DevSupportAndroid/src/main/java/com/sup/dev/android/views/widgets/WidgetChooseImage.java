package com.sup.dev.android.views.widgets;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.image_loader.ImageLoader;
import com.sup.dev.android.libs.image_loader.ImageLoaderFile;
import com.sup.dev.android.tools.ToolsAndroid;
import com.sup.dev.android.tools.ToolsBitmap;
import com.sup.dev.android.tools.ToolsFiles;
import com.sup.dev.android.tools.ToolsPermission;
import com.sup.dev.android.tools.ToolsToast;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.cards.Card;
import com.sup.dev.android.views.dialogs.DialogWidget;
import com.sup.dev.android.views.screens.SWidget;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.libs.debug.Debug;

import java.io.File;
import java.io.IOException;

public class WidgetChooseImage extends WidgetRecycler {

    private final RecyclerCardAdapter adapter;

    private Callback2<WidgetChooseImage, Bitmap> onSelected;
    private Callback onError;
    private boolean imagesLoaded;

    public WidgetChooseImage() {
        adapter = new RecyclerCardAdapter();

        vRecycler.setLayoutManager(new GridLayoutManager(view.getContext(), ToolsAndroid.isScreenPortrait() ? 3 : 6));

        setAdapter(adapter);
    }

    @Override
    public void onShow() {
        super.onShow();
        loadImages();

        ToolsView.setTextOrGone(vTitle, vTitle.getText());
        ((ViewGroup.MarginLayoutParams) vRecycler.getLayoutParams()).setMargins(0, ToolsView.dpToPx(2), 0, 0);

        if (viewWrapper instanceof DialogWidget)
            ((ViewGroup.MarginLayoutParams) vRecycler.getLayoutParams()).setMargins(ToolsView.dpToPx(8), ToolsView.dpToPx(2), ToolsView.dpToPx(8), 0);
        else if (viewWrapper instanceof SWidget) {
            vTitle.setVisibility(View.GONE);
            ((SWidget) viewWrapper).setTitle(vTitle.getText().toString());
        }
    }

    private void loadImages() {
        if (imagesLoaded) return;

        ToolsPermission.requestReadPermission(() -> {
            imagesLoaded = true;
            String[] projection = new String[]{MediaStore.Images.ImageColumns.DATA};
            Cursor cursor = SupAndroid.appContext.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

            while (cursor.moveToNext()) adapter.add(new CardImage(new File(cursor.getString(0))));
        }, () -> ToolsToast.show(SupAndroid.TEXT_ERROR_PERMISSION_READ_FILES));


    }

    //
    //  Setters
    //

    public WidgetChooseImage setOnSelected(Callback2<WidgetChooseImage, Bitmap> onSelected) {
        this.onSelected = onSelected;
        return this;
    }

    public WidgetChooseImage setOnError(Callback onError) {
        this.onError = onError;
        return this;
    }

    //
    //  Card
    //

    private class CardImage extends Card {

        private final File file;

        public CardImage(File file) {
            this.file = file;
        }

        @Override
        public int getLayout() {
            return R.layout.sheet_choose_image_card;
        }

        @Override
        public void bindView(View view) {
            ImageView vImage = view.findViewById(R.id.image);
            vImage.setOnClickListener(v -> {
                if (onSelected != null)
                    try {
                        onSelected.callback(WidgetChooseImage.this, ToolsBitmap.decode(ToolsFiles.readFile(file)));
                    } catch (IOException e) {
                        Debug.log(e);
                        if (onError != null) onError.callback();
                    }
                hide();
            });

            ImageLoader.load(new ImageLoaderFile(file)
                    .setImage(vImage)
                    .cashScaledBytes()
                    .sizes(512, 512)
                    .options(ImageLoader.OPTIONS_RGB_565())
                    .cropSquare());
        }


    }


}
