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

// получение уведомлений от приложения

//BroadcastReceiver - получает сигнал от AlarmManager и запускает уведомление
class NotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        showNotification(context) //вывести уведомление
        scheduleAlarm(context) // установить время следующего уведомления
    }
}

//отображение уведомления
fun showNotification(context: Context){
    val notification = NotificationCompat.Builder(context, "channel") //уведомление по каналу "channel"
        .setSmallIcon(R.drawable.icons8_ai_64_1)
        .setContentTitle("wanna ask something?")
        .setContentText("It's time to ask AI and learn something new!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    //у более новых версий нужно проверять, есть ли разрешение на отправку уведомлений
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
        == PackageManager.PERMISSION_GRANTED
    ) {
        //отправляем уведомление, если есть разрешение
        NotificationManagerCompat.from(context).notify(2001, notification)
    }
}

//установка времени следующего уведомления
fun scheduleAlarm(context: Context){
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    //создание Intent'а для задержки
    val intent = Intent(context, NotificationReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context, 0, intent, PendingIntent.FLAG_IMMUTABLE
    )

    //время следующего срабатывания
    val time = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(8)

    //точное время срабатывания, даже если в режиме энергосбережения
    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        time,
        pendingIntent
    )
}


//создание канала для уведомления

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

//код запроса на разрешение отправки уведомлений
const val REQUEST_CODE_NOTIFICATIONS = 101

//запрос на разрешение показа уведомлений
fun checkAndRequestNotificationPermission(activity: Activity) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 33+
        //если нет разрешения
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            //отправить запрос на получение разрешения
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_CODE_NOTIFICATIONS
            )
        }
    }
}
