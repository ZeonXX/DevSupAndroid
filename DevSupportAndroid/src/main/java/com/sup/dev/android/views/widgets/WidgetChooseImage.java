package com.sup.dev.android.views.widgets;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sup.dev.android.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.image_loader.ImageLoader;
import com.sup.dev.android.libs.image_loader.ImageLoaderFile;
import com.sup.dev.android.tools.ToolsAndroid;
import com.sup.dev.android.tools.ToolsBitmap;
import com.sup.dev.android.tools.ToolsFiles;
import com.sup.dev.android.tools.ToolsPermission;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsToast;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.adapters.recycler_view.RecyclerCardAdapter;
import com.sup.dev.android.views.cards.Card;
import com.sup.dev.android.views.dialogs.DialogSheetWidget;
import com.sup.dev.android.views.dialogs.DialogWidget;
import com.sup.dev.android.views.views.ViewIcon;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.tools.ToolsBytes;
import com.sup.dev.java.tools.ToolsNetwork;

import java.io.File;
import java.io.IOException;

public class WidgetChooseImage extends WidgetRecycler {

    private final RecyclerCardAdapter adapter;

    private Callback2<WidgetChooseImage, byte[]> onSelected;
    private boolean imagesLoaded;

    public WidgetChooseImage() {
        adapter = new RecyclerCardAdapter();
        View vFabGalleryContainer = ToolsView.inflate(R.layout.view_fab);
        View vFabLinkContainer = ToolsView.inflate(R.layout.view_fab);
        FloatingActionButton vFabGallery = vFabGalleryContainer.findViewById(R.id.fab);
        FloatingActionButton vFabLink = vFabLinkContainer.findViewById(R.id.fab);
        vContainer.addView(vFabGalleryContainer);
        vContainer.addView(vFabLinkContainer);

        ((ViewGroup.MarginLayoutParams) vFabLinkContainer.getLayoutParams()).rightMargin = ToolsView.dpToPx(72);

        vRecycler.setLayoutManager(new GridLayoutManager(view.getContext(), ToolsAndroid.isScreenPortrait() ? 3 : 6));

        vFabGallery.setImageResource(R.drawable.ic_landscape_white_24dp);
        vFabLink.setImageResource(R.drawable.ic_insert_link_white_24dp);
        vFabGallery.setOnClickListener(v -> openGallery());
        vFabLink.setOnClickListener(v -> showLink());

        setAdapter(adapter);
    }

    @Override
    public void onShow() {
        super.onShow();
        loadImages();

        ((ViewGroup.MarginLayoutParams) vRecycler.getLayoutParams()).setMargins(0, ToolsView.dpToPx(2), 0, 0);
        vRecycler.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

        if (viewWrapper instanceof DialogWidget)
            ((ViewGroup.MarginLayoutParams) vRecycler.getLayoutParams()).setMargins(ToolsView.dpToPx(8), ToolsView.dpToPx(2), ToolsView.dpToPx(8), 0);
        else if (viewWrapper instanceof DialogSheetWidget)
            vRecycler.getLayoutParams().height = ToolsView.dpToPx(320);
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
        }, () -> {
            ToolsToast.show(SupAndroid.TEXT_ERROR_PERMISSION_READ_FILES);
            hide();
        });


    }

    private void loadLink(String link) {
        WidgetProgressTransparent progress = ToolsView.showProgressDialog();
        Debug.log(" > " + link);
        ToolsNetwork.getBytesFromURL(link, bytes -> {
            progress.hide();

            if (!ToolsBytes.isImage(bytes)) {
                ToolsToast.show(SupAndroid.TEXT_ERROR_CANT_LOAD_IMAGE);
                return;
            }

            onSelected(bytes);
        });
    }

    private void showLink() {
        new WidgetField()
                .setMediaCallback((w, s) -> {
                    w.hide();
                    loadLink(s);
                })
                .enableFastCopy()
                .setHint(SupAndroid.TEXT_APP_LINK)
                .setOnEnter(SupAndroid.TEXT_APP_CHOOSE,
                        (w, s) -> loadLink(s))
                .setOnCancel(SupAndroid.TEXT_APP_CANCEL)
                .asSheetShow();

    }

    private void openGallery() {
        ToolsBitmap.getFromGallery(file -> {
            try {
                onSelected(ToolsFiles.readFile(file));
            } catch (IOException e) {
                Debug.log(e);
                ToolsToast.show(SupAndroid.TEXT_ERROR_CANT_LOAD_IMAGE);
            }
        });
    }

    private void onSelected(byte[] bytes) {
        if (onSelected != null) onSelected.callback(WidgetChooseImage.this, bytes);
        hide();
    }

    //
    //  Setters
    //

    public WidgetChooseImage setOnSelected(Callback2<WidgetChooseImage, byte[]> onSelected) {
        this.onSelected = onSelected;
        return this;
    }

    public WidgetChooseImage setOnSelectedBitmap(Callback2<WidgetChooseImage, Bitmap> callback) {
        this.onSelected = (widgetChooseImage, bytes) -> callback.callback(WidgetChooseImage.this, ToolsBitmap.decode(bytes));
        return this;
    }

    //
    //  Card
    //

    private class CardImage extends Card {

        private final File file;
        private byte[] bytes;

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
                if (bytes != null) onSelected(bytes);
            });

            ImageLoader.load(new ImageLoaderFile(file)
                    .setImage(vImage)
                    .cashScaledBytes()
                    .onLoaded(bytes -> this.bytes = bytes)
                    .sizes(512, 512)
                    .options(ImageLoader.OPTIONS_RGB_565())
                    .cropSquare());
        }


    }

}
