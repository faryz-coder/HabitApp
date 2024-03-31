package com.bmh.habitapp.startup

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bmh.habitapp.AuthActivity
import com.bmh.habitapp.manager.SharePreferencesManager
import com.bmh.habitapp.screen.Screen
import com.bmh.habitapp.screen.login.AppLogo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    navHostController: NavHostController,
    sharedPref: SharePreferencesManager? = null,
) {
    val scope = rememberCoroutineScope()
    val intent = Intent(navHostController.context, AuthActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

    scope.apply {
        scope.launch {
            delay(3000L)
            sharedPref?.isFirstTime().let {
                if (it != true) navHostController.navigate(Screen.Startup.route) else navHostController.context.startActivity(intent)
            }
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AppLogo()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    SplashScreen(navHostController = rememberNavController())
}