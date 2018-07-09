package com.sup.dev.android.tools;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.NotificationCompat;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.java.classes.callbacks.simple.Callback;

import java.util.HashMap;

public class ToolsBackgroundWork {

    //
    //  Foreground service
    //

    private static Notification notification;

    public static void startForegroundService(Class<? extends Activity> activityClass, @DrawableRes int icon, String title, String body){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(SupAndroid.appContext, ToolsNotifications.getDefChanelId())
                .setSmallIcon(icon)
                .setContentText(body)
                .setOngoing(true);
        if (title != null) builder.setContentTitle(title);

        Intent intent = new Intent(SupAndroid.appContext, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(SupAndroid.appContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        notification = builder.build();
    }

    public static class ForegroundService extends Service{

        @Override
        public void onCreate() {
            super.onCreate();
            startForeground(SupAndroid.SERVICE_FOREGROUND, notification);
        }

        public IBinder onBind(Intent intent) {
            return null;
        }
    }

    //
    //  Job scheduler
    //

    private static final HashMap<Integer, Callback> callbacks = new HashMap<>();
    private static int index;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED)
    public static void scheduleRepiteJob(int time, Callback callback){
        int myIndex = ++index;
        callbacks.put(myIndex, callback);
        scheduleJobIfNeed(myIndex, time);
    }

    public static void unscheduleRepiteJob(Callback callback){
        for(Integer i : callbacks.keySet()) if(callbacks.get(i) == callback) callbacks.remove(i);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED)
    private static void scheduleJobIfNeed(int myIndex, long time) {
        JobScheduler scheduler = (JobScheduler) SupAndroid.appContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        PersistableBundle bundle = new PersistableBundle();
        bundle.putLong("TIME", time);

        JobInfo job = new JobInfo.Builder(myIndex, new ComponentName(SupAndroid.appContext, NetworkSchedulerService.class))
                .setMinimumLatency(time)
                .setExtras(bundle)
                .setPersisted(true)
                .build();

        scheduler.schedule(job);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static class NetworkSchedulerService extends JobService {

        @Override
        @RequiresPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED)
        public boolean onStartJob(JobParameters params) {
            int jobId = params.getJobId();
            Callback callback = callbacks.get(jobId);
            if(callback != null) {
                scheduleJobIfNeed(jobId, params.getExtras().getLong("TIME"));
                callback.callback();
            }
            stopSelf();
            return false;
        }

        @Override
        public boolean onStopJob(JobParameters params) {
            return false;
        }
    }



}
