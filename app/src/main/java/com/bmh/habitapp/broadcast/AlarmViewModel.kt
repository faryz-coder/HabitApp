package com.bmh.habitapp.broadcast

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.ViewModel

class AlarmViewModel : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    lateinit var activity: Activity

    /**
     * Create Notification for reminder or daily quote
     * type:
     * 0 -> reminder
     * 1 -> quote
     */
    @SuppressLint("ScheduleExactAlarm")
    fun createNotification(type: Int) {
        val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(activity, AlarmReceiver::class.java)

        if (type == 1) {
            alarmIntent.putExtra("notification_id", 1)
            alarmIntent.putExtra("notification_index", 0)

            // Create reminder notification
            val pendingIntent = PendingIntent.getBroadcast(
                activity, type, alarmIntent, PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.add(Calendar.MINUTE, 1)

            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
            Log.d("AlarmViewModel", "alarm set for reminder")
        } else {
            // Create daily quote notification
            alarmIntent.putExtra("notification_id", 0)
            alarmIntent.putExtra("index", 0)

            // Set the times to trigger the alarm (10:00 AM, 3:00 PM, 8:00 PM)
            val alarmTime = listOf(10, 15, 20)

            for (time in alarmTime) {
                // Create an intent for the BroadcastReceiver
                val pendingIntent = PendingIntent.getBroadcast(
                    activity, type, // Unique Request code on time
                    alarmIntent, PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )

                // Set the trigger time for today
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, time)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                }

                // Schedule the alarm to repeat every day
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                ).let {
                    Log.d("AlarmViewModel", "alarm set for daily quote")
                }
            }
        }
    }

    fun removeNotification(type: Int) {
        val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(activity, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            activity, type, alarmIntent, PendingIntent.FLAG_MUTABLE
        )

        // Cancel the alarm
        alarmManager.cancel(pendingIntent)
    }

}