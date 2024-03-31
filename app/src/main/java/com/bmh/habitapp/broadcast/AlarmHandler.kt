package com.bmh.habitapp.broadcast

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.util.Log

class AlarmHandler(val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val alarmIntent = Intent(context, AlarmReceiver::class.java)
    @SuppressLint("ScheduleExactAlarm")
    fun setReminder(type: Int, index: Int) {
        if (type == 1) {
            alarmIntent.putExtra("notification_id", type)
            alarmIntent.putExtra("notification_index", index)
            // Create reminder notification
            val pendingIntent = PendingIntent.getBroadcast(
                context, type, alarmIntent, PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.add(Calendar.MINUTE, 30)

            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
            Log.d("AlarmViewModel", "next reminder set $index")
        }
    }
}