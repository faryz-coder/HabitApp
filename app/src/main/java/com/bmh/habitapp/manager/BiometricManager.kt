package com.bmh.habitapp.manager

import android.app.Activity
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor

class BiometricManager(val activity: Activity) {
     private lateinit var executor: Executor
     private lateinit var biometricPrompt: BiometricPrompt
     private lateinit var promptInfo: BiometricPrompt.PromptInfo

    fun biometricPrompt(context: FragmentActivity, onSuccess: () -> Unit = {}, onFailed: () -> Unit = {}) {
        executor = ContextCompat.getMainExecutor(activity)

        val callbacks =  object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int,
                                               errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(activity,
                    "Authentication error: $errString", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess.invoke()
                Toast.makeText(activity,
                    "Authentication succeeded!", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onFailed.invoke()
                Toast.makeText(activity, "Authentication failed",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }

        biometricPrompt = BiometricPrompt(context, executor,callbacks)

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}