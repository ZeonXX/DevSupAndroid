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
import com.sup.dev.android.libs.screens.navigator.Navigator;
import com.sup.dev.android.tools.ToolsAndroid;
import com.sup.dev.android.tools.ToolsBitmap;
import com.sup.dev.android.tools.ToolsPermission;
import com.sup.dev.android.tools.ToolsStorage;
import com.sup.dev.android.tools.ToolsToast;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.adapters.recycler_view.RecyclerCardAdapter;
import com.sup.dev.android.views.cards.Card;
import com.sup.dev.android.views.dialogs.DialogSheetWidget;
import com.sup.dev.android.views.dialogs.DialogWidget;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.tools.ToolsBytes;
import com.sup.dev.java.tools.ToolsNetwork;

import java.io.File;

public class WidgetChooseImage extends WidgetRecycler {

    private final RecyclerCardAdapter adapter;

    private Callback2<WidgetChooseImage, File> onSelected;
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
        vFabGallery.setOnClickListener(v -> ToolsBitmap.getFromGallery(b -> onSelected(b)));
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

    private void showLink() {
        new WidgetField()
                .setHint(SupAndroid.TEXT_APP_LINK)
                .setOnEnter(SupAndroid.TEXT_APP_CHOOSE,
                        (w, s) -> {
                            WidgetProgressTransparent progress = ToolsView.showProgressDialog();
                            ToolsNetwork.getBytesFromURL(s, bytes -> {
                                if (!ToolsBytes.isImage(bytes)) {
                                    progress.hide();
                                    ToolsToast.show(SupAndroid.TEXT_ERROR_CANT_LOAD_IMAGE);
                                    return;
                                }
                                String ex = null;
                                if (ToolsBytes.isPng(bytes)) ex = "png";
                                if (ToolsBytes.isJpg(bytes)) ex = "jpg";
                                if (ToolsBytes.isGif(bytes)) ex = "gif";
                                if (ex == null) {
                                    progress.hide();
                                    ToolsToast.show(SupAndroid.TEXT_ERROR_CANT_LOAD_IMAGE);
                                    return;
                                }

                                ToolsStorage.saveFileInDownloadFolder(bytes, ex,
                                        file -> {
                                            progress.hide();
                                            onSelected(file);
                                        },
                                        () -> {
                                            progress.hide();
                                            ToolsToast.show(SupAndroid.TEXT_ERROR_PERMISSION_READ_FILES);
                                        });

                            });
                        })
                .setOnCancel(SupAndroid.TEXT_APP_CANCEL)
                .asSheetShow();
    }

    private void onSelected(File file) {
        if (onSelected != null) onSelected.callback(WidgetChooseImage.this, file);
        hide();
    }

    //
    //  Setters
    //

    public WidgetChooseImage setOnSelected(Callback2<WidgetChooseImage, File> onSelected) {
        this.onSelected = onSelected;
        return this;
    }

    public WidgetChooseImage setOnSelectedBitmap(Callback2<WidgetChooseImage, Bitmap> callback) {
        this.onSelected =
                (widgetChooseImage, file) -> ToolsBitmap.getFromFile(file,
                        bitmap -> callback.callback(WidgetChooseImage.this, bitmap));
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
            vImage.setOnClickListener(v -> onSelected(file));

            ImageLoader.load(new ImageLoaderFile(file)
                    .setImage(vImage)
                    .cashScaledBytes()
                    .sizes(512, 512)
                    .options(ImageLoader.OPTIONS_RGB_565())
                    .cropSquare());
        }


    }


}
