package com.bmh.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bmh.habitapp.manager.SharePreferencesManager
import com.bmh.habitapp.screen.Screen
import com.bmh.habitapp.startup.SplashScreen
import com.bmh.habitapp.startup.StartupScreen

@Composable
fun SetupStartupNavGraph(
    navHostController: NavHostController,
    sharedPref: SharePreferencesManager,
) {
    NavHost(navController = navHostController, startDestination = Screen.Splash.route) {
        composable(
            route = Screen.Splash.route
        ) {
            SplashScreen(navHostController, sharedPref)
        }
        composable(
            route = Screen.Startup.route
        ) {
            StartupScreen(navHostController, sharedPref)
        }
    }
}