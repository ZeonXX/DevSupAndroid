package com.sup.dev.android.tools;

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

public class ToolsNotifications{

    public static final String CHANEL_ID_PREFIX = "notifications_id_";
    public static final String CHANEL_ID_DEF = "_def";
    public static final String CHANEL_ID_HIGH = "_high";
    public static final String CHANEL_ID_SALIENT = "_salient";

    private static NotificationManager notificationManager;

    public static void init(String channelName) {
        notificationManager = (NotificationManager) SupAndroid.appContext.getSystemService(Context.NOTIFICATION_SERVICE);

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

    public static NotificationManager getNotificationManager() {
        if(notificationManager == null) throw new RuntimeException("You must call ToolsNotifications.init");
        return notificationManager;
    }

    //
    //  Notifications
    //

    public static void notification(@DrawableRes int icon, @StringRes int title, @StringRes int body, Class<? extends Activity> activityClass) {
        notification(icon, ToolsResources.getString(title), ToolsResources.getString(body), activityClass);
    }

    public static void notification(@DrawableRes int icon, String body, Class<? extends Activity> activityClass) {
        notification(icon, null, body, activityClass);
    }

    public static void notification(@DrawableRes int icon, String title, String body, Class<? extends Activity> activityClass) {
        notification(icon, title, body, activityClass, false);
    }

    public static void notification(@DrawableRes int icon, String title, String body, Class<? extends Activity> activityClass, boolean sound) {
        notification(icon, title, body, new Intent(SupAndroid.appContext, activityClass), false);
    }

    public static void notification(@DrawableRes int icon, String title, String body, Intent intent, boolean sound) {
        if(notificationManager == null) throw new RuntimeException("You must call ToolsNotifications.init");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(SupAndroid.appContext, getDefChanelId())
                .setSmallIcon(icon)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setContentText(body);
        if (title != null) builder.setContentTitle(title);
        if (sound) builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));


        PendingIntent pendingIntent = PendingIntent.getActivity(SupAndroid.appContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        if (notificationManager != null) notificationManager.notify(1, builder.build());

    }

    public static void hide() {
        if(notificationManager == null) throw new RuntimeException("You must call ToolsNotifications.init");
        NotificationManager mNotificationManager = (NotificationManager) SupAndroid.appContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(1);
    }

    //
    //  Getters
    //

    public static String getDefChanelId() {
        if(notificationManager == null) throw new RuntimeException("You must call ToolsNotifications.init");
        return CHANEL_ID_PREFIX + SupAndroid.appContext.getApplicationInfo().packageName + CHANEL_ID_DEF;
    }

    public static String getHighChanelId() {
        if(notificationManager == null) throw new RuntimeException("You must call ToolsNotifications.init");
        return CHANEL_ID_PREFIX + SupAndroid.appContext.getApplicationInfo().packageName + CHANEL_ID_HIGH;
    }

    public static String getSalientChanelId() {
        if(notificationManager == null) throw new RuntimeException("You must call ToolsNotifications.init");
        return CHANEL_ID_PREFIX + SupAndroid.appContext.getApplicationInfo().packageName + CHANEL_ID_SALIENT;
    }
}
