package com.sup.dev.android.utils.interfaces;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

public interface UtilsIntent {

    void startIntentForResult(Intent intent, Callback2<Integer, Intent> onResult);

    void onActivityResult(int requestCode, int resultCode, Intent resultIntent);

    void openApp(int stringID);

    //
    //  Intents
    //

    void startIntent(Intent intent, Callback onActivityNotFound);

    void startWeb(String link, Callback onActivityNotFound);

    void startApp(String packageName, Callback onActivityNotFound);

    void startApp(String packageName, Callback1<Intent> onIntentCreated, Callback onActivityNotFound);

    void startPlayMarket(String packageName, Callback onActivityNotFound);

    void startMail(String link, Callback onActivityNotFound);

    void startPhone(String phone, Callback onActivityNotFound);

    void shareImage(Bitmap bitmap, String text,  String providerKey,Callback onActivityNotFound);

    void shareFile(String patch, String providerKey, Callback onActivityNotFound);

    void shareFile(String patch, String providerKey, String type, Callback onActivityNotFound);

    void shareFile(Uri uri, Callback onActivityNotFound);

    void shareFile(Uri uri, String type, Callback onActivityNotFound);

    void shareFile(Uri uri, String type, String text, Callback onActivityNotFound);

    void shareText(String text, Callback onActivityNotFound);

    //
    //  Intents result
    //

    void getGalleryImage(Callback1<Uri> onResult, Callback onError);

    //
    //  Services / Activities
    //

    void startService(Class<? extends Service> serviceClass, Object... extras);

    void startActivity(Context viewContext, Class<? extends Activity> activityClass, Object... extras);

    void startActivity(Context viewContext, Class<? extends Activity> activityClass, Integer flags, Object... extras);

    void addExtras(android.content.Intent intent, Object... extras);

    void sendSalient(PendingIntent intent);

}
