package com.bmh.habitapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bmh.habitapp.broadcast.AlarmViewModel
import com.bmh.habitapp.manager.BiometricManager
import com.bmh.habitapp.screen.detail.DetailViewModel
import com.bmh.habitapp.ui.theme.IndomitableTheme
import com.bmh.habitapp.viewModel.MainViewModel
import com.bmh.navigation.SetupNavGraph

class MainActivity : FragmentActivity() {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var alarmViewModel: AlarmViewModel
    lateinit var navController: NavHostController
    private val mainViewModel: MainViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        alarmViewModel = ViewModelProvider(this)[AlarmViewModel::class.java]
        alarmViewModel.activity = this
        mainViewModel.biometricManager = BiometricManager(this)
        checkPermission()

        setContent {
            IndomitableTheme {
                navController = rememberNavController()

                SetupNavGraph(mainViewModel, navController, detailViewModel, alarmViewModel)
            }
        }

        /*setContent {
            IndomitableTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }*/
    }

    private fun initNotificationChannel() {
        val channelId = this.getString(R.string.app_name)
        val channelName = "Indomitable"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = "notification"
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermission() {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
                initNotificationChannel()
            } else {
                Log.i("Permission: ", "Denied")
            }
        }

        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.i("Permission: ", "Granted")
                initNotificationChannel()
            }

            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    IndomitableTheme {
        Greeting("Android")
    }
}