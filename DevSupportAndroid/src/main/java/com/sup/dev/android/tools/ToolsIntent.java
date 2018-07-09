package com.sup.dev.android.tools;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URLConnection;
import java.util.ArrayList;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.classes.items.Item2;
import com.sup.dev.java.tools.ToolsText;
import com.sup.dev.java.libs.debug.Debug;

public class ToolsIntent {

    private static final String SHARE_FOLDER = "sup_share_cash";

    private static int codeCounter = 0;
    private static final ArrayList<Item2<Integer, Callback2<Integer, Intent>>> progressIntents = new ArrayList<>();

    public static void startIntentForResult(Intent intent, Callback2<Integer, Intent> onResult) {
        if (codeCounter == 65000)
            codeCounter = 0;
        int code = codeCounter++;
        progressIntents.add(new Item2<>(code, onResult));
        SupAndroid.activity.startActivityForResult(intent, code);
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        for (Item2<Integer, Callback2<Integer, Intent>> pair : progressIntents)
            if (requestCode == pair.a1) {
                progressIntents.remove(pair);
                pair.a2.callback(resultCode, resultIntent);
                return;
            }

    }

    public static void openApp(int stringID) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(ToolsResources.getString(stringID)));
        SupAndroid.appContext.startActivity(intent);
    }

    //
    //  Intents
    //

    public static void startIntent(Intent intent, Callback onActivityNotFound) {
        try {
            SupAndroid.appContext.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Debug.log(ex);
            if (onActivityNotFound != null)
                onActivityNotFound.callback();
        }
    }


    public static void startWeb(String link, Callback onActivityNotFound) {
        startIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(ToolsText.castToWebLink(link)))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), onActivityNotFound);
    }

    public static void startApp(String packageName, Callback onActivityNotFound) {
        startApp(packageName, null, onActivityNotFound);
    }

    public static void startApp(String packageName, Callback1<Intent> onIntentCreated, Callback onActivityNotFound) {
        PackageManager manager = SupAndroid.appContext.getPackageManager();
        Intent intent = manager.getLaunchIntentForPackage(packageName);
        if (intent == null) {
            if (onActivityNotFound != null) onActivityNotFound.callback();
            return;
        }
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        if (onIntentCreated != null) onIntentCreated.callback(intent);
        startIntent(intent, onActivityNotFound);
    }

    public static void startPlayMarket(String packageName, Callback onActivityNotFound) {
        startIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName + "&reviewId=0"))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), onActivityNotFound);
    }

    public static void startMail(String link, Callback onActivityNotFound) {
        startIntent(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + link))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), onActivityNotFound);
    }

    public static void startPhone(String phone, Callback onActivityNotFound) {
        startIntent(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), onActivityNotFound);
    }

    public static void shareImage(Bitmap bitmap, String text, String providerKey, Callback onActivityNotFound) {

        File[] files = new File(getCashRoot()).listFiles();
        if (files != null)
            for (File f : files)
                if (f.getName().contains("x_share_i"))
                    f.delete();

        new File(getCashRoot()).mkdirs();

        String patch = getCashRoot() + System.currentTimeMillis() + "_x_share_i.png";

        OutputStream out;
        File file = new File(patch);
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            Debug.log(e);
        }

        try {
            SupAndroid.activity.startActivity(Intent.createChooser(new Intent(Intent.ACTION_SEND)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(SupAndroid.activity, providerKey, file))
                    .putExtra(Intent.EXTRA_TEXT, text)
                    .setType("image/*"), null));
        } catch (ActivityNotFoundException ex) {
            Debug.log(ex);
            if (onActivityNotFound != null) onActivityNotFound.callback();
        }

    }

    public static void shareFile(String patch, String providerKey, Callback onActivityNotFound) {
        Uri fileUti = FileProvider.getUriForFile(SupAndroid.appContext, providerKey, new File(patch));
        shareFile(fileUti, onActivityNotFound);
    }

    public static void shareFile(String patch, String providerKey, String type, Callback onActivityNotFound) {
        Uri fileUti = FileProvider.getUriForFile(SupAndroid.appContext, providerKey, new File(patch));
        shareFile(fileUti, type, onActivityNotFound);
    }

    public static void shareFile(Uri uri, Callback onActivityNotFound) {
        shareFile(uri, URLConnection.guessContentTypeFromName(uri.toString()), onActivityNotFound);
    }

    public static void shareFile(Uri uri, String type, Callback onActivityNotFound) {
        shareFile(uri, type, null, onActivityNotFound);
    }

    public static void shareFile(Uri uri, String type, String text, Callback onActivityNotFound) {
        try {
            Intent i = new Intent()
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .setAction(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_STREAM, uri)
                    .setType(type);
            if (text == null) i.putExtra(Intent.EXTRA_TEXT, text);
            SupAndroid.activity.startActivity(Intent.createChooser(i, null));
        } catch (ActivityNotFoundException ex) {
            Debug.log(ex);
            if (onActivityNotFound != null) onActivityNotFound.callback();
        }
    }

    public static void shareText(String text, Callback onActivityNotFound) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
                .putExtra(Intent.EXTRA_TEXT, text);
        startIntent(Intent.createChooser(sharingIntent, null), onActivityNotFound);
    }

    //
    //  Intents result
    //

    public static void getGalleryImage(Callback1<Uri> onResult, Callback onError) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        try {
            startIntentForResult(Intent.createChooser(intent, null), (resultCode, resultIntent) -> {
                if (resultCode == Activity.RESULT_OK)
                    onResult.callback(resultIntent.getData());
                else if (onError != null) onError.callback();
            });
        } catch (ActivityNotFoundException ex) {
            Debug.log(ex);
            if (onError != null) onError.callback();
        }
    }

    //
    //  Services / Activities
    //


    public static void startServiceForeground(Class<? extends Service> serviceClass, Object... extras) {
        Intent intent = new Intent(SupAndroid.appContext, serviceClass);
        addExtras(intent, extras);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            SupAndroid.appContext.startForegroundService(intent);
        else
            SupAndroid.appContext.startService(intent);

    }

    public static void startService(Class<? extends Service> serviceClass, Object... extras) {
        Intent intent = new Intent(SupAndroid.appContext, serviceClass);
        addExtras(intent, extras);
        SupAndroid.appContext.startService(intent);
    }


    public static void startActivity(Context viewContext, Class<? extends Activity> activityClass, Object... extras) {
        startActivity(viewContext, activityClass, null, extras);
    }

    public static void startActivity(Context viewContext, Class<? extends Activity> activityClass, Integer flags, Object... extras) {
        Intent intent = new Intent(viewContext, activityClass);

        addExtras(intent, extras);

        if (flags != null)
            intent.addFlags(flags);

        viewContext.startActivity(intent);
    }

    public static void addExtras(Intent intent, Object... extras) {
        for (int i = 0; i < extras.length; i += 2) {
            Object extra = extras[i + 1];
            if (extra instanceof Parcelable)
                intent.putExtra((String) extras[i], (Parcelable) extra);
            else if (extra instanceof Serializable)
                intent.putExtra((String) extras[i], (Serializable) extra);
            else
                throw new IllegalArgumentException("Extras must be instance of Parcelable or Serializable");
        }
    }

    public static void sendSalient(PendingIntent intent) {
        try {
            intent.send();
        } catch (PendingIntent.CanceledException ex) {
            Debug.log(ex);
        }
    }

    //
    //  Support
    //

    private static String getCashRoot() {
        return SupAndroid.appContext.getExternalCacheDir().getAbsolutePath() + SHARE_FOLDER;
    }


}
