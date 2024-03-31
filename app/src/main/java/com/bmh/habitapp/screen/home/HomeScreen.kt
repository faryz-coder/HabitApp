package com.bmh.habitapp.screen.home

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissValue
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberSwipeToDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bmh.habitapp.AuthActivity
import com.bmh.habitapp.broadcast.AlarmViewModel
import com.bmh.habitapp.manager.AuthManager
import com.bmh.habitapp.manager.FirestoreManager
import com.bmh.habitapp.manager.SharePreferencesManager
import com.bmh.habitapp.model.BadHabits
import com.bmh.habitapp.model.MockListHabit
import com.bmh.habitapp.screen.Screen
import com.bmh.habitapp.screen.detail.DetailViewModel
import com.bmh.habitapp.utils.ImageUtils
import com.bmh.habitapp.viewModel.MainViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    navHostController: NavHostController,
    detailViewModel: DetailViewModel,
    alarmViewModel: AlarmViewModel
) {
    val context = LocalContext.current
    val list: MutableList<BadHabits> = remember { mutableStateListOf() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var checkQuote by remember { mutableStateOf(false) }
    var checkReminder by remember { mutableStateOf(false) }
    var checkBiometric by remember { mutableStateOf(false) }
    val sharePref = SharePreferencesManager(alarmViewModel.activity)

    checkQuote = sharePref.getNotificationDailyQuote()
    checkReminder = sharePref.getNotificationRemember()
    checkBiometric = sharePref.isBiometricEnabled()

    var email = remember {
        mutableStateOf("")
    }
    var rank = remember {
        mutableStateOf("")
    }

    detailViewModel.email.observe(LocalLifecycleOwner.current, Observer {
        email.value = it
        rank.value = detailViewModel.loginInfo.rank
    })


    try {
        FirestoreManager().getListHabit(
            onSuccess = {
                if (list != it) {
                    list.clear()
                    list.addAll(it)
                    detailViewModel.getUser()
                }
            },
            onFailed = {}
        )
    } catch (e: Exception) {
        if (list.isEmpty()) list.addAll(MockListHabit())
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(125.dp)
                                    .border(width = 1.dp, color = Color.Black, shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = rank.value,
                                    textAlign = TextAlign.Center,
                                    fontSize = 50.sp
                                )
                            }
                            Text(text = email.value)
                        }
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Text(text = "Settings")
                        HorizontalDivider()
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Quote of the day",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    Switch(checked = checkQuote, onCheckedChange = {
                                        checkQuote = it
                                        sharePref.setNotificationDailyQuote(it)
                                        if (it) {
                                            scope.launch {
                                                alarmViewModel.createNotification(0)
                                            }
                                        } else {
                                            scope.launch {
                                                alarmViewModel.removeNotification(0)
                                            }
                                        }
                                    })
                                }
                                Text(text = "Enable push notifications for daily quotes that bring motivation! ")
                            }
                        }
                        HorizontalDivider()
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Reminder Notifications",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    Switch(
                                        checked = checkReminder,
                                        onCheckedChange = {
                                            checkReminder = it
                                            sharePref.setNotificationRemember(it)
                                            if (it) {
                                                scope.launch {
                                                    alarmViewModel.createNotification(1)
                                                }
                                            } else {
                                                scope.launch {
                                                    alarmViewModel.removeNotification(1)
                                                }
                                            }
                                        })
                                }
                                Text(text = "Enable push notifications for reminders!")
                            }
                        }
                        HorizontalDivider()
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Biometric Login",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    Switch(
                                        checked = checkBiometric,
                                        onCheckedChange = {
                                            if (it) {
                                                scope.launch {
                                                    mainViewModel.biometricManager.biometricPrompt(
                                                        context as FragmentActivity,
                                                        onSuccess = {
                                                            sharePref.enableBiometric(it)
                                                            checkBiometric = it
                                                        }
                                                    )
                                                }
                                            } else {
                                                scope.launch {
                                                    sharePref.enableBiometric(it)
                                                    checkBiometric = it
                                                }
                                            }
                                        })
                                }
                            }
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        HorizontalDivider()
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "$ Currency")
                            Text(text = "MYR (RM)")
                        }
                    }


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "HabitIQ", fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Button(onClick = {
                            AuthManager().signOut()
                            // Remove biometric
                            sharePref.enableBiometric(false)

                            val intent = Intent(alarmViewModel.activity, AuthActivity::class.java)
                            alarmViewModel.activity.startActivity(intent)
                            alarmViewModel.activity.finish()

                        }) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(text = "Logout")
                                Icon(Icons.Rounded.Logout, contentDescription = "Logout")
                            }

                        }
                    }
                }
            }

        }
    ) {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 5.dp, end = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    IconButton(onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }) {
                        Icon(Icons.Rounded.Menu, contentDescription = "Menu")
                    }
                    IconButton(onClick = {
                        navHostController.navigate(Screen.Chat.route)
                    }) {
                        Icon(Icons.Rounded.ChatBubbleOutline, contentDescription = "Menu")
                    }
                }
                LazyColumn(
                    modifier = Modifier.weight(0.9f),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = list,
                        key = { item ->
                            item.id
                        }
                    ) { item ->
                        val cardCaptureView = remember {
                            mutableStateOf(
                                TestHabitItem(
                                    context,
                                    navHostController,
                                    mainViewModel,
                                    sharePref,
                                    item,
                                    detailViewModel
                                )
                            )
                        }
                        val dismissState =
                            rememberSwipeToDismissState(positionalThreshold = { 700f })
                        if (dismissState.dismissDirection == SwipeToDismissValue.EndToStart && dismissState.currentValue == SwipeToDismissValue.EndToStart) {
                            list.remove(item)
                            detailViewModel.getUser()
                            FirestoreManager().deleteHabits(item.id)
                        }

                        if (dismissState.dismissDirection == SwipeToDismissValue.StartToEnd && dismissState.currentValue == SwipeToDismissValue.StartToEnd && dismissState.targetValue == SwipeToDismissValue.StartToEnd) {
                            dismissState.apply {
                                scope.launch {
                                    capture(cardCaptureView.value, context)
                                    dismissState.reset()
                                }
                            }
                        }


                        SwipeToDismissBox(
                            enableDismissFromStartToEnd = true,
                            state = dismissState,
                            backgroundContent = {
                                if (dismissState.dismissDirection == SwipeToDismissValue.StartToEnd) {
                                    SharePreview()
                                } else {
                                    DeletePreview()
                                }

                            },
                            modifier = Modifier.clickable {
                                if (checkBiometric) {
                                    mainViewModel.biometricManager.biometricPrompt(context as FragmentActivity,
                                        onSuccess = {
                                            detailViewModel.badHabits = item
//                                            navHostController.navigate(Screen.Details.route)
                                        })
                                } else {
                                    detailViewModel.badHabits = item
//                                    navHostController.navigate(Screen.Details.route)
                                }
                            }
                        ) {
                            AndroidView(
                                modifier = Modifier.wrapContentSize(),
                                factory = {
                                    TestHabitItem(
                                        it,
                                        navHostController,
                                        mainViewModel,
                                        sharePref,
                                        item,
                                        detailViewModel
                                    ).apply {
                                        post {
                                            cardCaptureView.value = this
                                        }
                                    }
                                }
                            )
                        }
                    }
                }


                ElevatedButton(
                    onClick = { navHostController.navigate(Screen.AddHabit.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)

                ) {
                    Icon(Icons.Rounded.AddCircleOutline, contentDescription = "")
                }
            }
        }
    }
}


fun capture(view: TestHabitItem, context: Context) {
    val bitmap = ImageUtils.generateBitmap(view)
    shareImage(bitmap = bitmap, context)
}


private fun shareImage(bitmap: Bitmap, context: Context) {

    // Save the bitmap to app cache directory
    val file = File(context.cacheDir, "badHabit.png")
    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    outputStream.flush()
    outputStream.close()

    val contentUri =
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

    // Share the content
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, contentUri)
        type = "image/png"
//        setPackage("com.whatsapp")
    }

    // Grant read permission to the receiving app
    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    runBlocking {
        context.startActivity(Intent.createChooser(shareIntent, "Share Habit"))

    }
}

@Composable
fun DeletePreview() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Rounded.Delete,
            contentDescription = null
        )
    }
}

@Composable
fun SharePreview() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.CenterStart
    ) {
        Icon(
            imageVector = Icons.Rounded.Share,
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        mainViewModel = MainViewModel(),
        navHostController = rememberNavController(),
        detailViewModel = DetailViewModel(),
        alarmViewModel = AlarmViewModel()
    )
}