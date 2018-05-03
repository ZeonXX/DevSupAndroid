package com.sup.dev.android.utils.implementations;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationCompat;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsNotifications;

public class UtilsNotificationsImpl implements UtilsNotifications {

    public static final String CHANEL_ID_PREFIX = "notifications_id_";
    public static final String CHANEL_ID_DEF = "_def";
    public static final String CHANEL_ID_HIGH = "_high";
    public static final String CHANEL_ID_SALIENT = "_salient";

    private final NotificationManager notificationManager;

    public UtilsNotificationsImpl(String channelName) {
        notificationManager = (NotificationManager) SupAndroid.di.appContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel defChannel = new NotificationChannel(getDefChanelId(), channelName, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationChannel highChannel = new NotificationChannel(getHighChanelId(), channelName, NotificationManager.IMPORTANCE_HIGH);

            NotificationChannel salientChannel = new NotificationChannel(getSalientChanelId(), channelName, NotificationManager.IMPORTANCE_MIN);
            salientChannel.setVibrationPattern(new long[]{0});
            salientChannel.enableVibration(true);
            salientChannel.setSound(null, null);

            notificationManager.createNotificationChannel(defChannel);
            notificationManager.createNotificationChannel(highChannel);
            notificationManager.createNotificationChannel(salientChannel);
        }

    }

    @Override
    public NotificationManager getNotificationManager() {
        return notificationManager;
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

        NotificationCompat.Builder builder = new NotificationCompat.Builder(SupAndroid.di.appContext(), getDefChanelId())
                .setSmallIcon(icon)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setContentText(body);
        if (title != null) builder.setContentTitle(title);
        if (sound) builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));


        PendingIntent pendingIntent = PendingIntent.getActivity(SupAndroid.di.appContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        if (notificationManager != null) notificationManager.notify(1, builder.build());

    }

    //
    //  Getters
    //

    public String getDefChanelId() {
        return CHANEL_ID_PREFIX + SupAndroid.di.appContext().getApplicationInfo().packageName + CHANEL_ID_DEF;
    }

    public String getHighChanelId() {
        return CHANEL_ID_PREFIX + SupAndroid.di.appContext().getApplicationInfo().packageName + CHANEL_ID_HIGH;
    }

    public String getSalientChanelId() {
        return CHANEL_ID_PREFIX + SupAndroid.di.appContext().getApplicationInfo().packageName + CHANEL_ID_SALIENT;
    }
}

