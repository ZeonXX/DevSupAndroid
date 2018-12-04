package com.sup.dev.android.tools

import android.app.*
import android.content.Context
import android.content.Intent
import android.support.annotation.DrawableRes
import android.support.v4.app.NotificationCompat
import com.sup.dev.android.app.SupAndroid


object ToolsNotifications {

    enum class Importance {
        DEFAULT, HIGH, MIN
    }

    enum class GroupingType {
        SINGLE, GROUP
    }

    private var groupingPoolSize = 100
    private var groupingPoolCounter = 0

    private var notificationManager: NotificationManager? = null

    fun init() {
        if (notificationManager != null) return
        notificationManager = SupAndroid.appContext!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun instanceGroup(groupId: Int, name: Int) = instanceGroup(groupId, ToolsResources.getString(name))

    fun instanceGroup(groupId: Int, name: String): String {
        val id = "group_$groupId"
        if (ToolsNotifications.notificationManager != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            notificationManager!!.createNotificationChannelGroup(NotificationChannelGroup(id, name))

        return id
    }

    fun instanceChanel(id: Int) = Chanel(id)

    //
    //  Chanel
    //

    class Chanel {


        private val id: Int
        private val idS: String
        private val groupingPoolStart: Int
        private val groupingPoolEnd: Int
        private var name: String = ""
        private var description: String = ""
        private var groupId: String = ""
        private var sound: Boolean = true
        private var vibration: Boolean = true
        private var importance: Importance = Importance.DEFAULT
        private var groupingType = GroupingType.GROUP

        private var groupingPoolIndex: Int

        constructor(id: Int) {
            this.id = id
            this.idS = "chanel_$id"
            groupingPoolStart = groupingPoolCounter
            groupingPoolCounter += groupingPoolSize
            groupingPoolEnd = groupingPoolStart + groupingPoolSize
            groupingPoolIndex = groupingPoolStart
        }

        fun post(
                @DrawableRes icon: Int,
                title: String?,
                body: String?,
                intent: Intent) {

            val builder = NotificationCompat.Builder(SupAndroid.appContext!!, idS)
                    .setSmallIcon(icon)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setContentText(body)

            if (title != null) builder.setContentTitle(title)
            if (sound) {
                builder.setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE or Notification.DEFAULT_SOUND)
            } else {
                builder.setDefaults(Notification.DEFAULT_LIGHTS)
                builder.setSound(null)
                builder.setVibrate(LongArray(0))
            }

            val pendingIntent = PendingIntent.getActivity(SupAndroid.appContext!!, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            builder.setContentIntent(pendingIntent)

            if (groupingType == GroupingType.GROUP) {
                groupingPoolIndex++
                if (groupingPoolIndex == groupingPoolEnd) groupingPoolIndex = groupingPoolStart
            } else {
                groupingPoolIndex = groupingPoolStart
            }

            val notification = builder.build()

            if (notificationManager != null) notificationManager!!.notify(groupingPoolIndex, notification)

        }

        fun init(): Chanel {
            init(sound)
            return this
        }

        private fun init(sound: Boolean) {

            if (name.isEmpty()) name = SupAndroid.TEXT_APP_NAME ?: ""

            if (ToolsNotifications.notificationManager != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                val imp = when (importance) {
                    Importance.HIGH -> NotificationManager.IMPORTANCE_HIGH
                    Importance.MIN -> NotificationManager.IMPORTANCE_MIN
                    else -> NotificationManager.IMPORTANCE_DEFAULT
                }

                val channel = NotificationChannel(idS, name, imp)

                channel.enableVibration(true)
                if (groupId.isNotEmpty()) channel.group = groupId
                if (description.isNotEmpty()) channel.description = description
                if (!sound) channel.setSound(null, null)
                if (!vibration) channel.vibrationPattern = longArrayOf(0)

                notificationManager!!.createNotificationChannel(channel)
            }
        }

        //
        //  Setters
        //

        fun setName(name: Int): Chanel {
            return setName(ToolsResources.getString(name))
        }

        fun setDescription(description: Int): Chanel {
            return setName(ToolsResources.getString(description))
        }

        fun setDescription(description: String): Chanel {
            this.description = description
            return this
        }

        fun setName(name: String): Chanel {
            this.name = name
            return this
        }

        fun setSound(sound: Boolean): Chanel {
            this.sound = sound
            return this
        }

        fun setGroupId(groupId: String): Chanel {
            this.groupId = groupId
            return this
        }

        fun setVibration(vibration: Boolean): Chanel {
            this.vibration = vibration
            return this
        }

        fun setGroupingType(groupingType: GroupingType): Chanel {
            this.groupingType = groupingType
            return this
        }

        fun setImportance(importance: Importance): Chanel {
            this.importance = importance
            return this
        }


    }

}

