package com.bmh.habitapp.manager

import android.app.Activity
import android.content.Context
import com.bmh.habitapp.R

class SharePreferencesManager(val activity: Activity) {

    private val sharedPref = activity.getSharedPreferences(
        activity.getString(R.string.app_name), Context.MODE_PRIVATE
    ) ?: null

    fun getNotificationRemember(): Boolean {
        return sharedPref?.getBoolean(activity.getString(R.string.remember), false) ?: false
    }

    fun getNotificationDailyQuote(): Boolean {
        return sharedPref?.getBoolean(activity.getString(R.string.daily_quote), false) ?: false
    }


    fun setNotificationRemember(status: Boolean) {
        with(sharedPref?.edit()) {
            this?.putBoolean(activity.getString(R.string.remember), status)
            this?.apply()
        }
    }

    fun setNotificationDailyQuote(status: Boolean) {
        with(sharedPref?.edit()) {
            this?.putBoolean(activity.getString(R.string.daily_quote), status)
            this?.apply()
        }
    }

    fun setNotFirstTime() {
        with(sharedPref?.edit()) {
            this?.putBoolean(activity.getString(R.string.first_time), true)
            this?.apply()
        }
    }

    fun isFirstTime(): Boolean {
        return sharedPref?.getBoolean(activity.getString(R.string.first_time), false) ?: false
    }

    fun enableBiometric(isBiometricEnabled: Boolean) {
        val email = AuthManager().auth.currentUser?.email
        if (isBiometricEnabled) {
            with(sharedPref?.edit()) {
                this?.putString(activity.getString(R.string.email), email)
                this?.putBoolean(activity.getString(R.string.biometric), true)
                this?.apply()
            }
        } else {
            with(sharedPref?.edit()) {
                this?.putString(activity.getString(R.string.email), "")
                this?.putBoolean(activity.getString(R.string.biometric), false)
                this?.apply()
            }
        }
    }

    fun isBiometricEnabled(): Boolean {
        return sharedPref?.getBoolean(activity.getString(R.string.biometric), false) ?: false
    }
}