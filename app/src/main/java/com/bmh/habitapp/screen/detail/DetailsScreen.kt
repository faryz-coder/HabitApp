package com.bmh.habitapp.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CardGiftcard
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bmh.habitapp.screen.Screen
import com.bmh.navigation.SetupBottomNavGraph

@Composable
fun DetailsScreen(navHostController: NavHostController, detailViewModel: DetailViewModel) {
    val bottomNavController = rememberNavController()

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
//                Bottom Nav
                SetupBottomNavGraph(bottomNavController, detailViewModel, navHostController)
            }
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            ) {
                bottomNav(bottomNavController)
            }
        }
    }
}

@Composable
fun bottomNav(navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(
                    onClick = {
                        if (navController.currentDestination?.route != Screen.ItemDetail.route) {
                            navController.popBackStack(Screen.ItemDetail.route, false)
                        }
                    },
                    content = { Icon(Icons.Rounded.Info, contentDescription = "Icon Info") })
                IconButton(
                    onClick = {
                        if (navController.currentDestination?.route != Screen.Reward.route) {
                            navController.navigate(Screen.Reward.route)
                        }
                    },
                    content = {
                        Icon(
                            Icons.Rounded.CardGiftcard,
                            contentDescription = "Icon Gift"
                        )
                    })
                IconButton(
                    onClick = {
                        if (navController.currentDestination?.route != Screen.Score.route) {
                            navController.navigate(Screen.Score.route)
                        }
                    },
                    content = {
                        Icon(
                            Icons.Rounded.AutoAwesome,
                            contentDescription = "Icon Score"
                        )
                    })
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DetailsScreenPreview() {
    DetailsScreen(navHostController = rememberNavController(), detailViewModel = DetailViewModel())
}
