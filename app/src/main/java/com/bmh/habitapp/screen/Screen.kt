package com.bmh.habitapp.screen

sealed class Screen(val route: String) {

    // Startup Nav
    data object Splash: Screen(route = "splash_screen")
    data object Startup: Screen(route = "startup_screen")

    //
    data object SignIn: Screen(route = "sign_in_screen")
    data object SignUp: Screen(route = "sign_up_screen")
    data object Home: Screen(route = "home_screen")
    data object AddHabit: Screen(route = "add_habit_screen")
    data object Details: Screen(route = "details_screen")
    data object Chat: Screen(route = "chat_screen")

    // Bottom Nav
    data object ItemDetail: Screen(route = "item_detail_screen")
    data object Reward: Screen(route = "rewards_screen")
    data object Dialog: Screen(route = "dialog_screen")
    data object Score: Screen(route = "score_screen")

}