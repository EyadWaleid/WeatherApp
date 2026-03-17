package com.example.weatherapp

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class NotificationService : Service() {
    override fun onBind(intent: Intent): IBinder? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "DISMISS_NOTIFICATION") {
            val notificationId = intent.getIntExtra(WorkerKeys.NOTIFICATION_ID.key, -1)
            getSystemService(NotificationManager::class.java).cancel(notificationId)
            stopSelf()
            return START_NOT_STICKY
        }

        val alertName = intent?.getStringExtra(WorkerKeys.ALERT_NAME.key) ?: return START_NOT_STICKY
        val alertType = intent.getStringExtra(WorkerKeys.ALARM_TYPE.key) ?: return START_NOT_STICKY
        val temp = intent.getIntExtra(WorkerKeys.TEMP.key, 0)
        val tempDesc = intent.getStringExtra(WorkerKeys.TEMP_NAME.key) ?: return START_NOT_STICKY
        val notificationId = intent.getIntExtra(WorkerKeys.NOTIFICATION_ID.key, -1)
        if (notificationId == -1) return START_NOT_STICKY

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                notificationId,
                startNotification(alertName, tempDesc, temp, alertType, notificationId),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            )
        } else {
            startForeground(
                notificationId,
                startNotification(
                    alertName, tempDesc, temp, alertType,
                    notificationId = notificationId
                )
            )
        }

        return START_NOT_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannels() {
        val notificationManager = getSystemService(NotificationManager::class.java)

        val notificationChannel = NotificationChannel(
            "channel_notification",
            "Weather Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val alarmChannel = NotificationChannel(
            "channel_alarm",
            "Weather Alarms",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            enableVibration(true)
            enableLights(true)
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            setSound(
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
                audioAttributes
            )
        }

        notificationManager.createNotificationChannel(notificationChannel)
        notificationManager.createNotificationChannel(alarmChannel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startNotification(
        alertName: String,
        weatherDesc: String,
        temp: Int,
        alertType: String,
        notificationId: Int
    ): Notification {
        val (channelId, priority) = when {
            weatherDesc.contains("Thunderstorm", ignoreCase = true) ||
                    weatherDesc.contains("Tornado", ignoreCase = true) ||
                    weatherDesc.contains("rain", ignoreCase = true) || alertType.contains(
                "alarm",
                ignoreCase = true
            ) -> {
                "channel_alarm" to NotificationCompat.PRIORITY_HIGH
            }

            else -> {
                "channel_notification" to NotificationCompat.PRIORITY_DEFAULT
            }
        }
        val dismissIntent = Intent(this, NotificationService::class.java).apply {
            action = "DISMISS_NOTIFICATION"
            putExtra(WorkerKeys.NOTIFICATION_ID.key, notificationId)
        }
        val dismissPendingIntent = PendingIntent.getService(
            this,
            notificationId,
            dismissIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(alertName)
            .setContentText("$weatherDesc • ${temp}°")
            .setPriority(priority)
            .setOngoing(true)
            .setSmallIcon(R.drawable.alert)
            .addAction(R.drawable.outline_delete_24, "Dismiss", dismissPendingIntent)
            .build()
    }
}