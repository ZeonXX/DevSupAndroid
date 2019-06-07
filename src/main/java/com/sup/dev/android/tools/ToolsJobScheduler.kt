package com.sup.dev.android.tools

import android.annotation.SuppressLint
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.models.EventJonScheduler
import com.sup.dev.java.libs.debug.log
import com.sup.dev.java.libs.eventBus.EventBus

object ToolsJobScheduler{

    /*


    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />

     */

    @SuppressLint("MissingPermission")
    fun scheduleJob(id:Int, latency:Long) {
        log("scheduleJob $id $latency")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val scheduler = SupAndroid.appContext!!.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

            val job = JobInfo.Builder(id, ComponentName(SupAndroid.appContext!!, ToolsJobSchedulerService::class.java))
                    .setMinimumLatency(latency)
                    .setPersisted(true)
                    .build()

            scheduler.schedule(job)
        }
    }

    //
    //  Service
    //

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    class ToolsJobSchedulerService : JobService() {

        override fun onStartJob(params: JobParameters): Boolean {
            log("onStartJob")
            EventBus.post(EventJonScheduler(params))
            stopSelf()
            return false
        }

        override fun onStopJob(params: JobParameters): Boolean {
            log("onStopJob")
            return false
        }
    }

}