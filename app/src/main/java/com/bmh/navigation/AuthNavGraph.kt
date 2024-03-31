package com.bmh.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bmh.habitapp.AuthActivity
import com.bmh.habitapp.screen.Screen
import com.bmh.habitapp.screen.login.SignInScreen
import com.bmh.habitapp.screen.login.SignUpScreen

@Composable
fun AuthNavGraph(
    navHostController: NavHostController,
    authActivity: AuthActivity
) {
    NavHost(navController = navHostController, startDestination = Screen.SignIn.route) {
        composable(
            route = Screen.SignIn.route
        ) {
            SignInScreen(navHostController, authActivity)
        }
        composable(
            route = Screen.SignUp.route
        ) {
            SignUpScreen(navHostController, authActivity)
        }
    }
}