package com.sup.dev.android.views.screens;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.sup.dev.android.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.image_loader.ImageLoader;
import com.sup.dev.android.libs.image_loader.ImageLoaderId;
import com.sup.dev.android.libs.screens.Screen;
import com.sup.dev.android.tools.ToolsBitmap;
import com.sup.dev.android.tools.ToolsStorage;
import com.sup.dev.android.tools.ToolsToast;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.views.ViewGifImage;
import com.sup.dev.android.views.views.ViewIcon;
import com.sup.dev.android.views.widgets.Widget;
import com.sup.dev.android.views.widgets.WidgetProgressTransparent;
import com.sup.dev.java.tools.ToolsThreads;

public class SImageView extends Screen {

    private Bitmap bitmap;
    private long id;
    private boolean isGif;

    public SImageView(Bitmap bitmap) {
        this(bitmap, 0, false);
    }

    public SImageView(long id) {
        this(null, id, false);
    }

    public SImageView(long id, boolean isGif) {
        this(null, id, isGif);
    }

    private SImageView(Bitmap bitmap, long id, boolean isGif) {
        super(R.layout.screen_image_view);
        this.bitmap = bitmap;
        this.id = id;
        this.isGif = isGif;

        ViewGifImage vImage = findViewById(R.id.image);
        ViewIcon vDownload = findViewById(R.id.download);

        vImage.setClickable(false);
        vDownload.setOnClickListener(v -> download());

        if (bitmap != null)
            vImage.setImageBitmap(bitmap);
        else if (id > 0)
            vImage.init(isGif ? 0 : id, isGif ? id : 0);


    }

    private void download() {
        Widget dialog = ToolsView.showProgressDialog(SupAndroid.TEXT_APP_DOWNLOADING);
        ToolsThreads.INSTANCE.thread(() -> {
            if (bitmap != null) ToolsStorage.saveImageInDownloadFolder(bitmap, null);
            else if (id > 0) {
                ImageLoaderId.load(id, bytes -> {
                    if (!isGif)  ToolsStorage.saveImageInDownloadFolder(ToolsBitmap.decode(bytes), f -> ToolsToast.show(SupAndroid.TEXT_APP_DONE));
                    else ToolsStorage.saveFileInDownloadFolder(bytes, ".gif", f -> ToolsToast.show(SupAndroid.TEXT_APP_DONE), () -> ToolsToast.show(SupAndroid.TEXT_ERROR_PERMISSION_READ_FILES));
                });
            }
            dialog.hide();
            ToolsToast.show(SupAndroid.TEXT_APP_DOWNLOADED);
        });

    }

}
