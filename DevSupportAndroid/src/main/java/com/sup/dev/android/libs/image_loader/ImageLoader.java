package com.sup.dev.android.libs.image_loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.widget.ImageView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsBitmap;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.java.classes.collections.CashBytes;
import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.tools.ToolsThreads;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ImageLoader {

    static final CashBytes<Object> bitmapCash = new CashBytes<>(1024 * 1024 * ((android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ? 20 : 5));
    static final ArrayList<ImageLoaderA> turn = new ArrayList<>();
    static ThreadPoolExecutor threadPool;

    static {
        threadPool = new ThreadPoolExecutor(2, 4, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
    }

    public static BitmapFactory.Options OPTIONS_RGB_565() {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        return opt;
    }

    //
    //  Public
    //

    public static void unsubscribe(ImageView vImage) {

        if (vImage == null) return;

        for (int i = 0; i < turn.size(); i++)
            if (turn.get(i).vImage == vImage)
                turn.remove(i--);

    }

    public static void load(ImageLoaderA loader) {

        byte[] bytes = loader.noLoadFromCash ? null : bitmapCash.get(loader.getKey());

        if (bytes != null) {
            bitmapCash.reorderTop(loader.getKey());
            if (loader.onLoaded != null) loader.onLoaded.callback(bytes);
            if (loader.vImage != null) putImage(loader, parseImage(loader, bytes), false);
            return;
        }

        if (loader.vImage != null) {
            if (loader.holder > 0) {
                loader.vImage.setImageResource(loader.holder);
            } else if (loader.w != 0 && loader.h != 0) {
                Bitmap bitmap = Bitmap.createBitmap(loader.w, loader.h, Bitmap.Config.ARGB_4444);
                bitmap.eraseColor(ToolsResources.getColor(R.color.focus));
                loader.vImage.setImageBitmap(bitmap);
            } else {
                loader.vImage.setImageDrawable(new ColorDrawable(ToolsResources.getColor(R.color.focus)));
            }

            unsubscribe(loader.vImage);

        }

        turn.add(loader);

        for (ImageLoaderA l : turn) if (l.isKey(loader.getKey()) && l != loader) return;

        threadPool.execute(() -> {
            try {
                loadNow(loader);
            } catch (Throwable ex) {
                Debug.log(ex);
            }
        });
    }

    //
    //  Methods
    //

    private static void loadNow(ImageLoaderA loader) {

        if (!turn.contains(loader)) return;

        byte[] loadedBytes = loader.load();
        Bitmap bitmap = parseImage(loader, loadedBytes);
        byte[] bytes = loader.cashScaledBytes ? ToolsBitmap.toJPGBytes(bitmap, 100) : loadedBytes;

        ToolsThreads.main(() -> {

            if (!loader.noCash) bitmapCash.add(loader.getKey(), bytes);
            for (int i = 0; i < turn.size(); i++) {
                ImageLoaderA l = turn.get(i);
                if (l.isKey(loader.getKey())) {
                    if (l.onLoaded != null) l.onLoaded.callback(bytes);
                    if (l.vImage != null) putImage(l, bitmap, true);
                    turn.remove(i--);
                }
            }

        });

    }

    private static Bitmap parseImage(ImageLoaderA loader, byte[] bytes) {
        Bitmap bm = ToolsBitmap.decode(bytes, loader.w, loader.h, loader.options, loader.cropSquareCenter);
        if (loader.cropSquareCenter) bm = ToolsBitmap.cropCenterSquare(bm);
        return bm;
    }

    private static void putImage(ImageLoaderA loader, Bitmap bm, boolean animate) {
        ToolsThreads.main(() -> {
            if (!loader.isKey(loader.vImage.getTag())) return;
            loader.vImage.setImageDrawable(new DrawableImageLoader(loader.vImage.getContext(), bm, animate && loader.fade));
        });
    }

}

