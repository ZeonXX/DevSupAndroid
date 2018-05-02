package com.sup.dev.android.utils.implementations;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationCompat;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsNotifications;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

import static android.content.Context.NOTIFICATION_SERVICE;

public class UtilsNotificationsImpl implements UtilsNotifications {

    public static final String CHANEL_ID_PREFIX = "notifications_id_";

    public UtilsNotificationsImpl(String channelName) {
        this(channelName, null);
    }

    public UtilsNotificationsImpl(String channelName, Callback1<NotificationChannel> onSetUp) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) SupAndroid.di.appContext().getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(getDefChanelId(), channelName, importance);

            if (onSetUp == null) {
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            } else {
                onSetUp.callback(mChannel);
            }

            mNotificationManager.createNotificationChannel(mChannel);
        }

    }

    //
    //  Notifications
    //

    public void notification(@DrawableRes int icon, @StringRes int title, @StringRes int body, Class<? extends Activity> activityClass) {
        notification(icon, SupAndroid.di.utilsResources().getString(title), SupAndroid.di.utilsResources().getString(body), activityClass);
    }

    public void notification(@DrawableRes int icon, String body, Class<? extends Activity> activityClass) {
        notification(icon, null, body, activityClass);
    }

    public void notification(@DrawableRes int icon, String title, String body, Class<? extends Activity> activityClass) {
        notification(icon, title, body, activityClass, false);
    }

    public void notification(@DrawableRes int icon, String title, String body, Class<? extends Activity> activityClass, boolean sound) {
        notification(icon, title, body, new Intent(SupAndroid.di.appContext(), activityClass), false);
    }

    public void notification(@DrawableRes int icon, String title, String body, Intent intent, boolean sound) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(SupAndroid.di.appContext(), getDefChanelId());
        builder.setSmallIcon(icon);
        builder.setLargeIcon(SupAndroid.di.utilsResources().getBitmap(icon));
        if (title != null) builder.setContentTitle(title);
        if (sound) builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setContentText(body);
        builder.setAutoCancel(true);

        PendingIntent pendingIntent = PendingIntent.getActivity(SupAndroid.di.appContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager mNotifyMgr = (NotificationManager) SupAndroid.di.appContext().getSystemService(NOTIFICATION_SERVICE);
        if (mNotifyMgr != null) mNotifyMgr.notify(1, builder.build());

    }

    public void hide() {
        NotificationManager mNotificationManager = (NotificationManager) SupAndroid.di.appContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(1);
    }

    //
    //  Getters
    //

    private String getDefChanelId() {
        return CHANEL_ID_PREFIX + SupAndroid.di.appContext().getApplicationInfo().packageName;
    }


}
