package com.example.aimessengerapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import androidx.core.app.ActivityCompat
import java.util.concurrent.TimeUnit

class NotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        showNotification(context)
        scheduleAlarm(context)
    }
}

fun showNotification(context: Context){
    val notification = NotificationCompat.Builder(context, "channel")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("wanna ask something?")
        .setContentText("It's time to ask AI and learn something new!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
        == PackageManager.PERMISSION_GRANTED
    ) {
        NotificationManagerCompat.from(context).notify(2001, notification)
    }
}

fun scheduleAlarm(context: Context){
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, NotificationReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context, 0, intent, PendingIntent.FLAG_IMMUTABLE
    )

    val time = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(8)

    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        time,
        pendingIntent
    )
}


fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Только для Android 8+ (API 26+)
        val channel = NotificationChannel(
            "channel",
            "NotificationAlarm",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "уведомления от AI"
        }

        // Регистрируем канал в системе
        val notificationManager: NotificationManager =
            context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}

const val REQUEST_CODE_NOTIFICATIONS = 101

fun checkAndRequestNotificationPermission(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 33+
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_CODE_NOTIFICATIONS
            )
        }
    }
}
