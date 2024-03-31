package com.bmh.habitapp.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import com.bmh.habitapp.manager.SharePreferencesManager
import com.bmh.habitapp.model.BadHabits
import com.bmh.habitapp.screen.Screen
import com.bmh.habitapp.screen.detail.DetailViewModel
import com.bmh.habitapp.utils.calculateEarned
import com.bmh.habitapp.utils.getAchievement
import com.bmh.habitapp.utils.percentBeforeNextLevel
import com.bmh.habitapp.viewModel.MainViewModel

@Composable
fun HabitsItem(
    navHostController: NavHostController? = null,
    mainViewModel: MainViewModel? = null,
    sharePref: SharePreferencesManager? = null,
    badHabits: BadHabits,
    detailViewModel: DetailViewModel
) {
    val context = LocalContext.current
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            if (sharePref !=  null && mainViewModel != null && sharePref.isBiometricEnabled()) {
                mainViewModel.biometricManager.biometricPrompt(context as FragmentActivity,
                    onSuccess = {
                        detailViewModel.badHabits = badHabits
                        navHostController?.navigate(Screen.Details.route)
                    })
            } else {
                detailViewModel.badHabits = badHabits
                navHostController?.navigate(Screen.Details.route)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = badHabits.habits,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
            )
            Box(
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(50.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Achievement",
                            color = Color.Black,
                        )
                        Text(
                            text = getAchievement(badHabits.beginDate),
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    // 2nd Row
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Earned",
                            color = Color.Black,
                        )
                        Text(
                            text = calculateEarned(
                                badHabits = badHabits,
                                detailViewModel = detailViewModel
                            ),
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    // 3rd Row
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Duration",
                            color = Color.Black,
                        )
                        Text(
                            text = badHabits.beginDate,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
            LinearProgressIndicator(
                progress = { percentBeforeNextLevel(badHabits.beginDate) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
