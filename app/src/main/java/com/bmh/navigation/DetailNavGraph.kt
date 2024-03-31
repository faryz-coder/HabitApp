package com.bmh.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bmh.habitapp.screen.Screen
import com.bmh.habitapp.screen.detail.DetailViewModel
import com.bmh.habitapp.screen.detail.screen.item_details.ItemDetailsScreen
import com.bmh.habitapp.screen.detail.screen.rewards.RewardScreen
import com.bmh.habitapp.screen.detail.screen.score.ScoreScreen

@Composable
fun SetupBottomNavGraph(
    navHostController: NavHostController,
    detailViewModel: DetailViewModel,
    mainNavHostController: NavHostController
) {
    NavHost(navController = navHostController, startDestination = Screen.ItemDetail.route ) {
        composable(
            route = Screen.ItemDetail.route
        ) {
            ItemDetailsScreen(mainNavHostController, detailViewModel)
        }
        composable(
            route = Screen.Reward.route
        ) {
            RewardScreen(navHostController, detailViewModel)
        }
        composable(
            route = Screen.Score.route
        ) {
            ScoreScreen(navHostController, detailViewModel)
        }
    }
}