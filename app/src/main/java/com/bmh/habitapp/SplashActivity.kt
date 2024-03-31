package com.bmh.habitapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bmh.habitapp.manager.SharePreferencesManager
import com.bmh.habitapp.ui.theme.IndomitableTheme
import com.bmh.navigation.SetupStartupNavGraph

class SplashActivity : ComponentActivity() {
    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref = SharePreferencesManager(this)
        setContent {
            IndomitableTheme {
                navController = rememberNavController()

                SetupStartupNavGraph(navHostController = navController, sharedPref)
            }
        }

    }
}