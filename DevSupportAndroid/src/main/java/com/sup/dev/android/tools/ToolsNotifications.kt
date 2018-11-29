package com.sup.dev.android.tools

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.app.NotificationCompat
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.java.classes.collections.ArrayListTemporary


object ToolsNotifications {

    val CHANEL_ID_PREFIX = "notifications_id_"
    val CHANEL_ID_DEF = "_def"
    val CHANEL_ID_HIGH = "_high"
    val CHANEL_ID_SALIENT = "_salient"

    var chanelNameDef = SupAndroid.TEXT_APP_NAME
    var chanelNameHigh = SupAndroid.TEXT_APP_NAME
    var chanelNameSalient = SupAndroid.TEXT_APP_NAME
    var soundLimit = 2
    val spundCounterList = ArrayListTemporary<Int>(1000L * 10)

    private var notificationManager: NotificationManager? = null

    //
    //  Getters
    //

    val defChanelId: String
        get() {
            init()
            return CHANEL_ID_PREFIX + SupAndroid.appContext!!.applicationInfo.packageName + CHANEL_ID_DEF
        }

    val highChanelId: String
        get() {
            init()
            return CHANEL_ID_PREFIX + SupAndroid.appContext!!.applicationInfo.packageName + CHANEL_ID_HIGH
        }

    val salientChanelId: String
        get() {
            init()
            return CHANEL_ID_PREFIX + SupAndroid.appContext!!.applicationInfo.packageName + CHANEL_ID_SALIENT
        }

    private fun init() {
        if (notificationManager != null) return
        notificationManager = SupAndroid.appContext!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (notificationManager != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val defChannel = NotificationChannel(defChanelId, chanelNameDef, NotificationManager.IMPORTANCE_DEFAULT)
            val highChannel = NotificationChannel(highChanelId, chanelNameHigh, NotificationManager.IMPORTANCE_HIGH)

            val salientChannel = NotificationChannel(salientChanelId, chanelNameSalient, NotificationManager.IMPORTANCE_MIN)
            salientChannel.vibrationPattern = longArrayOf(0)
            salientChannel.enableVibration(true)
            salientChannel.setSound(null, null)

            notificationManager!!.createNotificationChannel(defChannel)
            notificationManager!!.createNotificationChannel(highChannel)
            notificationManager!!.createNotificationChannel(salientChannel)
        }

    }

    fun getNotificationManager(): NotificationManager? {
        init()
        return notificationManager
    }

    //
    //  Notifications
    //

    fun notification(@DrawableRes icon: Int, @StringRes title: Int, @StringRes body: Int, activityClass: Class<out Activity>) {
        notification(icon, ToolsResources.getString(title), ToolsResources.getString(body), activityClass)
    }

    fun notification(@DrawableRes icon: Int, body: String, activityClass: Class<out Activity>) {
        notification(icon, null, body, activityClass)
    }

    @JvmOverloads
    fun notification(@DrawableRes icon: Int, title: String?, body: String?, activityClass: Class<out Activity>, sound: Boolean = false) {
        notification(icon, title, body, Intent(SupAndroid.appContext!!, activityClass), sound)
    }

    fun notification(@DrawableRes icon: Int, title: String?, body: String?, intent: Intent, sound: Boolean) {
        init()

        val builder = NotificationCompat.Builder(SupAndroid.appContext!!, defChanelId)
                .setSmallIcon(icon)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE)
                .setContentText(body)
        if (title != null) builder.setContentTitle(title)

        if (sound) {
            if(spundCounterList.size() < soundLimit) {
                spundCounterList.add(0)
                builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            }
        }

        val pendingIntent = PendingIntent.getActivity(SupAndroid.appContext!!, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pendingIntent)

        if (notificationManager != null) notificationManager!!.notify(1, builder.build())

    }

    fun hide() {
        init()
        val mNotificationManager = SupAndroid.appContext!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.cancel(1)
    }
}

