package com.sup.dev.android.utils.interfaces;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.CallbackPair;
import com.sup.dev.java.classes.callbacks.simple.CallbackSource;

public interface UtilsIntent {

    void startIntentForResult(Intent intent, CallbackPair<Integer, Intent> onResult);

    void onActivityResult(int requestCode, int resultCode, Intent resultIntent);

    void openApp(int stringID);

    //
    //  Intents
    //

    void startIntent(Intent intent, Callback onActivityNotFound);

    void startWeb(String link, Callback onActivityNotFound);

    void startApp(String packageName, Callback onActivityNotFound);

    void startApp(String packageName, CallbackSource<Intent> onIntentCreated, Callback onActivityNotFound);

    void startPlayMarket(String packageName, Callback onActivityNotFound);

    void startMail(String link, Callback onActivityNotFound);

    void startPhone(String phone, Callback onActivityNotFound);

    void shareFile(Activity activity, String patch, Callback onActivityNotFound);

    void shareFile(Activity activity, String patch, String type, Callback onActivityNotFound);

    void shareFile(Activity activity, Uri uri, Callback onActivityNotFound);

    void shareFile(Activity activity, Uri uri, String type, Callback onActivityNotFound);

    void shareText(String title, String text, Callback onActivityNotFound);

    //
    //  Intents result
    //

    void getGalleryImage(CallbackSource<Uri> onResult, Callback onError);

    //
    //  Services / Activities
    //

    void startService(Class<? extends Service> serviceClass, Object... extras);

    void startActivity(Context viewContext, Class<? extends Activity> activityClass, Object... extras);

    void startActivity(Context viewContext, Class<? extends Activity> activityClass, Integer flags, Object... extras);

    void addExtras(android.content.Intent intent, Object... extras);

    void sendSalient(PendingIntent intent);

}
