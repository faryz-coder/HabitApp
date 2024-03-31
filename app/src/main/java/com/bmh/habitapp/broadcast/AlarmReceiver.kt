package com.bmh.habitapp.broadcast

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bmh.habitapp.R


@SuppressLint("MissingPermission")
class AlarmReceiver : BroadcastReceiver() {

    private var quote = 0

    override fun onReceive(context: Context, intent: Intent?) {
        val listQuote = context.resources.getStringArray(R.array.daily_quote)
        val listReminder = context.resources.getStringArray(R.array.reminders)
        val notificationId = intent?.getIntExtra("notification_id", 0)
        val index = intent?.getIntExtra("notification_index", 0)

        Log.d("AlarmReceiver", "index $index, notif_id $notificationId")

        if (notificationId != null && index != null) {

            // Build and show the notification using NotificationManager
            var builder = NotificationCompat.Builder(context, context.getString(R.string.app_name))
                .setSmallIcon(R.drawable.habitiq_logo)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(
                    if (notificationId == 1) listReminder[index] else listQuote[quote]
                )
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(if (notificationId == 1) listReminder[index] else listQuote[quote])
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(context)) {
                notify(0, builder.build())

                // Increase count and set next alarm
                if (notificationId == 1) {
                    val nextIndex = if ((index + 1) >= listReminder.size) 0 else index + 1
                    AlarmHandler(context).setReminder(
                        notificationId,
                        nextIndex
                    )
                } else {
                    quote += 1
                }
                // Reset count
                if (quote > listQuote.size) quote = 0

            }
        }
    }

}