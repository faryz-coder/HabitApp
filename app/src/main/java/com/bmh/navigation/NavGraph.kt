package com.bmh.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bmh.habitapp.broadcast.AlarmViewModel
import com.bmh.habitapp.screen.Screen
import com.bmh.habitapp.screen.add_habit.AddHabitScreen
import com.bmh.habitapp.screen.chat.ChatScreen
import com.bmh.habitapp.screen.detail.DetailViewModel
import com.bmh.habitapp.screen.detail.DetailsScreen
import com.bmh.habitapp.screen.home.HomeScreen
import com.bmh.habitapp.viewModel.MainViewModel

@Composable
fun SetupNavGraph(
    mainViewModel: MainViewModel,
    navHostController: NavHostController,
    detailViewModel: DetailViewModel,
    alarmViewModel: AlarmViewModel
) {
    NavHost(navController = navHostController, startDestination = Screen.Home.route ) {
        composable(
            route = Screen.Home.route
        ) {
            HomeScreen(mainViewModel, navHostController, detailViewModel, alarmViewModel)
        }
        composable(
            route = Screen.AddHabit.route
        ) {
            AddHabitScreen(navHostController)
        }
        composable(
            route = Screen.Details.route
        ) {
            DetailsScreen(navHostController, detailViewModel)
        }
        composable(
            route = Screen.Chat.route
        ) {
            ChatScreen(navHostController, detailViewModel)
        }
    }
}