package com.sup.dev.android.tools

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PersistableBundle
import android.support.annotation.DrawableRes
import android.support.annotation.RequiresApi
import android.support.annotation.RequiresPermission
import android.support.v4.app.NotificationCompat
import com.sup.dev.android.app.SupAndroid
import java.lang.RuntimeException
import java.util.HashMap

object ToolsBackgroundWork {

    //
    //  Foreground service
    //

    private var notification: Notification? = null

    //
    //  Job scheduler
    //

    private val callbacks = HashMap<Int, ()->Unit>()
    private var index: Int = 0

    fun startForegroundService(activityClass: Class<out Activity>, @DrawableRes icon: Int, title: String?, body: String) {
        throw RuntimeException("Fix me")
        /*
        val builder = NotificationCompat.Builder(SupAndroid.appContext!!, ToolsNotifications.getDefChanelId())
                .setSmallIcon(icon)
                .setContentText(body)
                .setOngoing(true)
        if (title != null) builder.setContentTitle(title)

        val intent = Intent(SupAndroid.appContext!!, activityClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val pendingIntent = PendingIntent.getActivity(SupAndroid.appContext!!, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pendingIntent)

        notification = builder.build()

        ToolsIntent.startServiceForeground(ForegroundService::class.java)
        */
    }

    class ForegroundService : Service() {

        override fun onCreate() {
            super.onCreate()
            startForeground(SupAndroid.SERVICE_FOREGROUND, notification)
        }

        override fun onBind(intent: Intent): IBinder? {
            return null
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED)
    fun scheduleRepiteJob(time: Int, callback: ()->Unit) {
        val myIndex = ++index
        callbacks[myIndex] = callback
        scheduleJobIfNeed(myIndex, time.toLong())
    }

    fun unscheduleRepiteJob(callback: ()->Unit) {
        for (i in callbacks.keys) if (callbacks[i] === callback) callbacks.remove(i)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED)
    private fun scheduleJobIfNeed(myIndex: Int, time: Long) {
        val scheduler = SupAndroid.appContext!!.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        val bundle = PersistableBundle()
        bundle.putLong("TIME", time)

        val job = JobInfo.Builder(myIndex, ComponentName(SupAndroid.appContext!!, NetworkSchedulerService::class.java))
                .setMinimumLatency(time)
                .setExtras(bundle)
                .setPersisted(true)
                .build()

        scheduler.schedule(job)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    class NetworkSchedulerService : JobService() {

        @RequiresPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED)
        override fun onStartJob(params: JobParameters): Boolean {
            val jobId = params.jobId
            val callback = callbacks[jobId]
            if (callback != null) {
                scheduleJobIfNeed(jobId, params.extras.getLong("TIME"))
                callback.invoke()
            }
            stopSelf()
            return false
        }

        override fun onStopJob(params: JobParameters): Boolean {
            return false
        }
    }

}
