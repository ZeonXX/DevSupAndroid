package tests._sup_android.stubs.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.sup.dev.android.utils.interfaces.UtilsIntent;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.CallbackPair;
import com.sup.dev.java.classes.callbacks.simple.CallbackSource;

public class UtilsIntentStub implements UtilsIntent {

    public int onActivityResult_requestCode;
    public int onActivityResult_resultCode;
    public Intent onActivityResult_intent;
    public boolean startPlayMarketCalled;
    public String startPlayMarket_packageName;

    @Override
    public void startIntentForResult(Intent intent, CallbackPair<Integer, Intent> onResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        onActivityResult_requestCode = requestCode;
        onActivityResult_resultCode = resultCode;
        onActivityResult_intent = resultIntent;
    }

    @Override
    public void openApp(int stringID) {

    }

    @Override
    public void startIntent(Intent intent, Callback onActivityNotFound) {

    }

    @Override
    public void startWeb(String link, Callback onActivityNotFound) {

    }

    @Override
    public void startPlayMarket(String packageName, Callback onActivityNotFound) {
        startPlayMarketCalled = true;
        startPlayMarket_packageName = packageName;
    }

    @Override
    public void startMail(String link, Callback onActivityNotFound) {

    }

    @Override
    public void startPhone(String phone, Callback onActivityNotFound) {

    }

    @Override
    public void getGalleryImage(CallbackSource<Uri> onResult, Callback onError) {

    }

    @Override
    public void startService(Class<? extends Service> serviceClass, Object... extras) {

    }

    @Override
    public void startActivity(Context viewContext, Class<? extends Activity> activityClass, Object... extras) {

    }

    @Override
    public void startActivity(Context viewContext, Class<? extends Activity> activityClass, Integer flags, Object... extras) {

    }

    @Override
    public void addExtras(Intent intent, Object... extras) {

    }

    @Override
    public void sendSalient(PendingIntent intent) {

    }
}
