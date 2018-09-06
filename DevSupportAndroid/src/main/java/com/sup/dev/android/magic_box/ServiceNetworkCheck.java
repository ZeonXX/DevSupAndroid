package com.sup.dev.android.magic_box;


import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.annotation.WorkerThread;
import android.support.v4.app.NotificationCompat;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.eventbus_multi_process.EventBusMultiProcess;
import com.sup.dev.android.tools.ToolsIntent;
import com.sup.dev.android.tools.ToolsNotifications;
import com.sup.dev.android.tools.ToolsStorage;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.subscribed.SubscribedCallback1;
import com.sup.dev.java.classes.items.Item;
import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.libs.event_bus.EventBus;
import com.sup.dev.java.tools.ToolsThreads;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;


/*
    Грубое решение проблемы проверки доступа в интернет через DOZE мод.
    * DOZE не ограничевает фореграунд сервисы, но они должны ыть запушпущены в отдельно процесе.

    1. Проверяет доступ в интернет в рабочем потоке
    2. Если его нет, проверяет еще раз подождав 5 секунд
    3. Если его нет, но он был во время прошлой проверки, то проверяет фореграунд сервисе запущеном в отдельном процессе (обход DOZE)

        <service
            android:name="com.sup.dev.android.magic_box.ServiceNetworkCheck"
            android:process=":networkCheckProcess"/>

 */

public class ServiceNetworkCheck extends Service {


    private static final String P_KEY = "P_KEY";
    private static long globalKey;
    public static final String SERVICE_NETWORK_CHECK_LAST_RESULT = "SERVICE_NETWORK_CHECK_LAST_RESULT";
    public static int NOTIFICATION_ICON;
    public static String NOTIFICATION_TITLE;
    public static String NOTIFICATION_TEXT;

    //
    //  Static
    //

    private static void onCheckFinish(Callback1<Boolean> onResult, boolean has) {
        ToolsStorage.put(SERVICE_NETWORK_CHECK_LAST_RESULT, has);
        onResult.callback(has);
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static void check(Callback1<Boolean> onResult) {
        isHasInternetConnection(has -> {
            if (!has && ToolsStorage.getBoolean(SERVICE_NETWORK_CHECK_LAST_RESULT, true))
                isHasInternetConnectionInService(has2 -> onCheckFinish(onResult, has2));
            else
                onCheckFinish(onResult, has);
        });
    }

    public static void isHasInternetConnectionInService(Callback1<Boolean> onResult) {
        long key = ++globalKey;
        EventBus.subscribeHard(EventServiceNetworkCheck.class,
                new SubscribedCallback1<EventServiceNetworkCheck>(
                        e -> {
                            if (e.key == key) onResult.callback(e.result);
                        }
                ).setLifeTime(15000));
        ToolsIntent.startServiceForeground(ServiceNetworkCheck.class,
                P_KEY, key);
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static void isHasInternetConnection(Callback1<Boolean> onResult) {
        ToolsThreads.thread(() -> {
            Item<Boolean> has = new Item<>(isHasInternetConnectionNow());
            if (!has.getA()) {
                ToolsThreads.sleep(5000);
                has.setA(isHasInternetConnectionNow());
            }
            ToolsThreads.main(() -> onResult.callback(has.getA()));
        });
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    @WorkerThread
    public static boolean isHasInternetConnectionNow() {
        Socket sock = null;
        try {
            sock = new Socket();
            sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
            sock.close();
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            if (sock != null) {
                try {
                    sock.close();
                } catch (IOException e) {
                    Debug.log(e);
                }
            }
        }
    }


    //
    //  Service
    //

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public int onStartCommand(Intent intent, int flags, int startId) {
        long key = intent.getLongExtra(P_KEY, -1);
        startForeground(SupAndroid.SERVICE_NETWORK_CHECK, instanceNotification());
        isHasInternetConnection(has -> {
            EventBusMultiProcess.post(new EventServiceNetworkCheck(key, has));
            stopSelf();
        });
        return START_STICKY;
    }

    private Notification instanceNotification() {
        return new NotificationCompat.Builder(SupAndroid.appContext, ToolsNotifications.getSalientChanelId())
                .setSmallIcon(NOTIFICATION_ICON)
                .setAutoCancel(false)
                .setTicker(NOTIFICATION_TITLE)
                .setContentText(NOTIFICATION_TEXT)
                .setContentTitle(NOTIFICATION_TITLE)
                .setSound(null)
                .setVibrate(new long[]{0L})
                .setOngoing(true)
                .build();
    }

    //
    //  Event
    //

    public static class EventServiceNetworkCheck implements Serializable {

        long key;
        boolean result;

        public EventServiceNetworkCheck(long key, boolean result) {
            this.key = key;
            this.result = result;
        }

    }

}
